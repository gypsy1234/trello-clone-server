package app.query

import java.util.UUID

import app.query.CardQuery.CardQueryResult
import lib.TypeAlias.ProcessResult

import scala.concurrent.ExecutionContext

trait CardQuery {
  def get(id: UUID)(implicit ec: ExecutionContext): ProcessResult[CardQueryResult]
}

object CardQuery {
  case class CardQueryResult(
    id: UUID,
    listID: UUID,
    listTitle: String,
    title: String
  )
}
