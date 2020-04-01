package domain.sercice.interpreter

import java.util.UUID

import domain.model.{CardList, CardListId, CardListRepository, CardListTitle}
import domain.sercice.CardListService
import lib.TypeAlias.ProcessResult
import cats.instances.future._
import lib.DBOperation

import scala.concurrent.ExecutionContext

class CardListServiceInterpreter(
  cardListRepository: CardListRepository
) extends CardListService[CardList] {
  def add(title: CardListTitle)(implicit DBOperation: DBOperation, ec: ExecutionContext): ProcessResult[CardList] = {
    val cardList = CardList(CardListId(UUID.randomUUID()), title)
    for {
      _ <- cardListRepository.store(cardList)
    } yield cardList
  }
}
