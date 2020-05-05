package domain.service.interpreter

import java.util.UUID

import domain.model.{Card, CardId, CardListId, CardPosition, CardRepository, CardTitle}
import domain.service.CardService
import lib.TypeAlias.ProcessResult
import cats.instances.future._
import lib.DBOperation

import scala.concurrent.ExecutionContext

class CardServiceInterpreter(
  cardRepository: CardRepository
) extends CardService[Card] {

  def add(listId: CardListId, title: CardTitle, position: CardPosition)(implicit DBOperation: DBOperation, ec: ExecutionContext): ProcessResult[Card] = {
    val card = Card(CardId(UUID.randomUUID()), listId, title, position)
    for {
      _ <- cardRepository.store(card)
    } yield card
  }

  def update(id: CardId, title: CardTitle, position: CardPosition)(implicit DBOperation: DBOperation, ec: ExecutionContext): ProcessResult[Card] =
    for {
      before <- cardRepository.get(id)
      after = before.copy(title = title, position = position)
      _ <- cardRepository.store(after)
    } yield after
}
