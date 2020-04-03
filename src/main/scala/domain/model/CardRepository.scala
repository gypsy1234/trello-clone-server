package domain.model

import lib.DBOperation
import lib.TypeAlias.ProcessResult

trait CardRepository {
  def store(list: Card)(implicit dbOperation: DBOperation): ProcessResult[Unit]
  def get(listId: CardId)(implicit dbOperation: DBOperation): ProcessResult[Card]
}
