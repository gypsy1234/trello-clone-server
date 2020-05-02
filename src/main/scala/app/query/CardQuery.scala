package app.query

import java.util.UUID

import app.query.CardQuery.CardQueryResult
import lib.TypeAlias.ProcessResult

import scala.concurrent.ExecutionContext

trait CardQuery {
  def getByListId(listId: UUID)(implicit ec: ExecutionContext): ProcessResult[Seq[CardQueryResult]]
  def getAll(implicit ec: ExecutionContext): ProcessResult[Seq[CardQueryResult]]
  def getById(id: UUID)(implicit ec: ExecutionContext): ProcessResult[CardQueryResult]
}

object CardQuery {
  case class CardQueryResult(
    id: UUID,
    listId: UUID,
    listTitle: String,
    title: String
  )
}
