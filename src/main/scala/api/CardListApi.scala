package api

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.{RawHeader, `Content-Location`}
import akka.http.scaladsl.server.Route
import api.CardListApi.{CardListPostInput, CardListPostOutput}
import app.AppModule
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext

class CardListApi(
  val module: AppModule
)(implicit ec: ExecutionContext)
  extends ApiSupport
    with ApiErrorMapper {

  import module._

  def routes: Route =
    postCardList ~
      getCardLists ~
      getCardList

  def postCardList: Route =
    path("v1" / "card-lists") {
      post {
        entity(as[CardListPostInput]) { input =>
          extractMatchedPath { matchedPath =>
            val result = cardListCommand.add(input.title)
            response(result) { r =>
              respondWithHeaders(
                `Content-Location`(s"$matchedPath/${r.value.toString}")
              ) {
                complete((StatusCodes.Created, CardListPostOutput(r.value)))
              }
            }
          }
        }
      }
    }

  def getCardLists: Route =
    path("v1" / "card-lists") {
      get {
        val result = cardListQuery.get
        response(result) { r =>
          complete((StatusCodes.OK, r))
        }
      }
    }

  def getCardList: Route =
    path("v1" / "card-lists" / JavaUUID) { uuid =>
      get {
        val result = cardListQuery.getById(uuid)
        response(result) { r =>
          complete((StatusCodes.OK, r))
        }
      }
    }
}

object CardListApi {
  case class CardListPostInput(title: String)
  case class CardListPostOutput(id: UUID)
}
