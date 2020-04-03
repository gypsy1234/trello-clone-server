package domain.service

import domain.model.{CardListId, CardTitle}
import lib.DBOperation
import lib.TypeAlias.ProcessResult

import scala.concurrent.ExecutionContext

trait CardService[Card] {
  def add(listId: CardListId, title: CardTitle)(implicit DBOperation: DBOperation, ec: ExecutionContext): ProcessResult[Card]
}
