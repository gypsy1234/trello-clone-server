package domain.model

import lib.DBOperation
import lib.TypeAlias.ProcessResult

trait CardListRepository {
  def store(list: CardList)(implicit dbOperation: DBOperation): ProcessResult[Unit]
  def get(listId: CardListId)(implicit dbOperation: DBOperation): ProcessResult[CardList]
}
