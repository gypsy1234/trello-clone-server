package app.command

import cats.instances.future._
import domain.model.{CardList, CardListId, CardListTitle}
import domain.sercice.CardListService
import lib.DBContext
import lib.TypeAlias.ProcessResult

import scala.concurrent.ExecutionContext

class CardListCommand(
  dbContext: DBContext,
  cardListService: CardListService[CardList]
)(implicit val ec: ExecutionContext) {
  def add(title: String): ProcessResult[CardListId] =
    dbContext.tx { implicit op =>
      cardListService.add(CardListTitle(title)).map(_.id)
    }
}
