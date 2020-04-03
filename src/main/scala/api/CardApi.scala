package api

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import api.CardApi.{CardPostInput, CardPostOutput}
import app.AppModule
import domain.model.CardListId
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
      getCard

  def postCard: Route =
    path("v1" / "card-lists" / JavaUUID / "cards") { listId =>
      post {
        entity(as[CardPostInput]) { input =>
          val result = cardCommand.add(CardListId(listId), input.title)
          response(result) { r =>
            complete((StatusCodes.Created, CardPostOutput(r.value)))
          }
        }
      }
    }

  def getCard: Route =
    path("v1" / "cards" / JavaUUID) { uuid =>
      get {
        val result = cardQuery.get(uuid)
        response(result) { r =>
          complete((StatusCodes.OK, r))
        }
      }
    }
}

object CardApi {
  case class CardPostInput(title: String)
  case class CardPostOutput(id: UUID)
}