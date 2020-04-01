package it.test_lib

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.coding.{Gzip, NoCoding}
import akka.http.scaladsl.marshalling.{Marshal, Marshaller}
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.HttpEncodings
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.AutoDerivation

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

trait TestRestClient
  extends FailFastCirceSupport
    with AutoDerivation {

  implicit val system: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val baseUri = "http://localhost:9001"

  def post[I, O](
    path: String,
    input: I
  )(f: (HttpResponse, O) => Unit)(
    implicit
    m: Marshaller[I, RequestEntity],
    um: Unmarshaller[ResponseEntity, O]
  ): Unit =
    request(
      method = HttpMethods.POST,
      path = path,
      input = Some(input)
    )(f)

  def put[I, O](
    path: String,
    input: I
  )(f: (HttpResponse, O) => Unit)(
    implicit
    m: Marshaller[I, RequestEntity],
    um: Unmarshaller[ResponseEntity, O]
  ): Unit =
    request(
      method = HttpMethods.PUT,
      path = path,
      input = Some(input)
    )(f)

  def delete[I, O](
    path: String,
    input: I
  )(f: (HttpResponse, O) => Unit)(
    implicit
    m: Marshaller[I, RequestEntity],
    um: Unmarshaller[ResponseEntity, O]
  ): Unit =
    request(
      method = HttpMethods.DELETE,
      path = path,
      input = Some(input)
    )(f)

  def get[O](
    path: String
  )(f: (HttpResponse, O) => Unit)(
    implicit
    um: Unmarshaller[ResponseEntity, O]
  ): Unit =
    request[String, O, Unit](
      method = HttpMethods.GET,
      path = path,
      input = None
    )(f)


  private def requestInternal[I, T](
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


  def request[I, O, T](
    method: HttpMethod,
    path: String,
    input: Option[I]
  )(f: (HttpResponse, O) => T)(
    implicit
    m: Marshaller[I, RequestEntity],
    um: Unmarshaller[ResponseEntity, O]
  ): T = {

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


  def requestNoBody[I, T](
    method: HttpMethod,
    path: String,
    input: Option[I]
  )(f: HttpResponse => T)(
    implicit
    m: Marshaller[I, RequestEntity],
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


  def postNobody[I](
    path: String,
    input: I
  )(f: HttpResponse => Unit)(
    implicit
    m: Marshaller[I, RequestEntity]
  ): Unit =
    requestNoBody(
      method = HttpMethods.POST,
      path = path,
      input = Some(input)
    )(f)

  def putNobody[I](
    path: String,
    input: I
  )(f: HttpResponse => Unit)(
    implicit
    m: Marshaller[I, RequestEntity]
  ): Unit =
    requestNoBody(
      method = HttpMethods.PUT,
      path = path,
      input = Some(input)
    )(f)


  def noBody(
    path: String
  )(f: HttpResponse => Unit): Unit =
    requestNoBody[String, Unit](
      method = HttpMethods.GET,
      path = path,
      input = None
    )(f)

}

object TestRestClient extends TestRestClient
