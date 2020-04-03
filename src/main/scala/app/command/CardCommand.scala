package app.command

import cats.instances.future._
import domain.model.{Card, CardId, CardListId, CardTitle}
import domain.service.CardService
import lib.DBContext
import lib.TypeAlias.ProcessResult

import scala.concurrent.ExecutionContext

class CardCommand(
  dbContext: DBContext,
  cardService: CardService[Card]
)(implicit val ec: ExecutionContext) {
  def add(listId: CardListId, title: String): ProcessResult[CardId] =
    dbContext.tx { implicit op =>
      cardService.add(listId, CardTitle(title)).map(_.id)
    }
}
