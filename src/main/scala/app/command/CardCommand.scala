package app.command

import cats.instances.future._
import domain.model.{Card, CardId, CardListId, CardPosition, CardTitle}
import domain.service.CardService
import lib.DBContext
import lib.TypeAlias.ProcessResult

import scala.concurrent.ExecutionContext

class CardCommand(
  dbContext: DBContext,
  cardService: CardService[Card]
)(implicit val ec: ExecutionContext) {

  def add(listId: CardListId, title: String, position: Double): ProcessResult[CardId] =
    dbContext.tx { implicit op =>
      cardService.add(listId, CardTitle(title), CardPosition(position)).map(_.id)
    }

  def update(id: CardId, title: String, position: Double): ProcessResult[Unit] =
    dbContext.tx { implicit op =>
      cardService.update(id, CardTitle(title), CardPosition(position)).map(_ -> ())
    }
}
