package api

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.{RawHeader, `Content-Location`}
import akka.http.scaladsl.server.Route
import api.CardApi.{CardPostInput, CardPostOutput, CardPutInput}
import app.AppModule
import domain.model.{CardId, CardListId}
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext

class CardApi(
  val module: AppModule
)(implicit ec: ExecutionContext)
  extends ApiSupport
    with ApiErrorMapper {

  import module._

  def routes: Route =
    postCard ~
      putCard ~
      getCards ~
      getCard ~
      getCardsByCardList

  def postCard: Route =
    path("v1" / "card-lists" / JavaUUID / "cards") { listId =>
      post {
        entity(as[CardPostInput]) { input =>
          val result = cardCommand.add(CardListId(listId), input.title, input.position)
          response(result) { r =>
            respondWithHeaders(
              `Content-Location`(s"/v1/cards/${r.value.toString}") // ToDo ここいい感じにできないか？
            ) {
              complete((StatusCodes.Created, CardPostOutput(r.value)))
            }
          }
        }
      }
    }

  def putCard: Route =
    path("v1" / "cards" / JavaUUID) { id =>
      put {
        entity(as[CardPutInput]) { input =>
          val result = cardCommand.update(CardId(id), input.title, input.position)
          response(result) { _ =>
            complete(StatusCodes.NoContent)
          }
        }
      }
    }

  def getCards: Route =
    path("v1" / "cards") {
      get {
        val result = cardQuery.getAll
        response(result) { r =>
          complete((StatusCodes.OK, r))
        }
      }
    }

  def getCardsByCardList: Route =
    path("v1" / "card-lists" / JavaUUID / "cards") { listId =>
      get {
        val result = cardQuery.getByListId(listId)
        response(result) { r =>
          complete((StatusCodes.OK, r))
        }
      }
    }

  def getCard: Route =
    path("v1" / "cards" / JavaUUID) { uuid =>
      get {
        val result = cardQuery.getById(uuid)
        response(result) { r =>
          complete((StatusCodes.OK, r))
        }
      }
    }
}

object CardApi {
  case class CardPostInput(title: String, position: Double)
  case class CardPutInput(title: String, position: Double)
  case class CardPostOutput(id: UUID)
}
