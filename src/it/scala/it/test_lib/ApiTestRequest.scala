package it.test_lib

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.coding.{Gzip, NoCoding}
import akka.http.scaladsl.marshalling.{Marshal, Marshaller}
import akka.http.scaladsl.model.{HttpEntity, HttpMethod, HttpMethods, HttpRequest, HttpResponse, RequestEntity, ResponseEntity}
import akka.http.scaladsl.model.headers.HttpEncodings
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

object ApiTestRequest {

  implicit val system: ActorSystem = ActorSystem()

  def post[I, O](
    path: String,
    input: I
  )(implicit
    m: Marshaller[I, RequestEntity],
    um: Unmarshaller[ResponseEntity, O],
  ) =
    new ApiTestRequest[I, O](
      method = HttpMethods.POST,
      path = path,
      input = Some(input)
    )

  def put[I, O](
    path: String,
    input: I
  )(implicit
    m: Marshaller[I, RequestEntity],
    um: Unmarshaller[ResponseEntity, O],
  ) =
    new ApiTestRequest[I, O](
      method = HttpMethods.PUT,
      path = path,
      input = Some(input)
    )

  def delete[I, O](
    path: String,
    input: I
  )(implicit
    m: Marshaller[I, RequestEntity],
    um: Unmarshaller[ResponseEntity, O],
  ) =
    new ApiTestRequest[I, O](
      method = HttpMethods.DELETE,
      path = path,
      input = Some(input)
    )

  def get[O](
    path: String
  )(implicit
    um: Unmarshaller[ResponseEntity, O]
  ) =
    new ApiTestRequest[String, O](
      method = HttpMethods.GET,
      path = path,
      input = None
    )

}

class ApiTestRequest[I, O](
  method: HttpMethod,
  path: String,
  input: Option[I]
)(implicit
  m: Marshaller[I, RequestEntity],
  um: Unmarshaller[ResponseEntity, O],
  system: ActorSystem
) {

  val baseUri = "http://localhost:9001"

  implicit val ec: ExecutionContextExecutor = system.dispatcher

  def apply[T](f: (HttpResponse, O) => T)(): T = {
    val (res, out) =
      Await.result(
        for {
          (req, res) <- requestInternal(method, path, input)
          out <- Unmarshal(res.entity).to[O]
        } yield {
          println(s"responseBody=$out")
          (res, out)
        },
        30.seconds
      )
    f(res, out)
  }

  def success[T](f: O => T)(): T = {
    apply[T] { (_, out) => f(out) }
  }

  def noBody[T](
    f: HttpResponse => T
  ): T = {
    import akka.http.scaladsl.unmarshalling.PredefinedFromEntityUnmarshallers._

    val res =
      Await.result(
        for {
          (req, res) <- requestInternal(method, path, input)
          out <- Unmarshal(res.entity).to[String]
        } yield {
          println(s"responseBody=$out")
          res
        },
        30.seconds
      )
    f(res)
  }

  private def requestInternal[T](
    method: HttpMethod,
    path: String,
    input: Option[I]
  )(implicit
    m: Marshaller[I, RequestEntity],
  ): Future[(HttpRequest, HttpResponse)] = {

    def decodeResponse(response: HttpResponse): HttpResponse = {
      val decoder = response.encoding match {
        case HttpEncodings.gzip ⇒ Gzip
        case HttpEncodings.identity ⇒ NoCoding
        case _ => NoCoding
      }
      decoder.decodeMessage(response)
    }

    for {
      inputEntity <- input.map { i =>
        Marshal(i).to[RequestEntity].map(Some(_))
      }.getOrElse(Future.successful(None))
      req <- Future.successful {
        HttpRequest(
          method = method,
          uri = s"$baseUri$path",
          entity = inputEntity.getOrElse(HttpEntity.Empty)
        )
      }
      res <- {
        Http().singleRequest(req).map(decodeResponse)
      }
    } yield {
      println("<============================")
      println(s"$req ===> $res")
      println("============================>")
      (req, res)
    }
  }
}
