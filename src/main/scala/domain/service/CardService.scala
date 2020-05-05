package domain.service

import domain.model.{CardId, CardListId, CardPosition, CardTitle}
import lib.DBOperation
import lib.TypeAlias.ProcessResult

import scala.concurrent.ExecutionContext

trait CardService[Card] {
  def add(listId: CardListId, title: CardTitle, position: CardPosition)(implicit DBOperation: DBOperation, ec: ExecutionContext): ProcessResult[Card]
  def update(id: CardId, title: CardTitle, position: CardPosition)(implicit DBOperation: DBOperation, ec: ExecutionContext): ProcessResult[Card]
}
