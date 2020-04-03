package domain.service

import domain.model.CardListTitle
import lib.DBOperation
import lib.TypeAlias.ProcessResult

import scala.concurrent.ExecutionContext

trait CardListService[CardList] {
  def add(title: CardListTitle)(implicit DBOperation: DBOperation, ec: ExecutionContext): ProcessResult[CardList]
}
