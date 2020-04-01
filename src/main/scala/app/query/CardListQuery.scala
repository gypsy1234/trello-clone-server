package app.query

import java.util.UUID

import app.query.CardListQuery.CardListQueryResult
import lib.TypeAlias.ProcessResult

import scala.concurrent.ExecutionContext

trait CardListQuery {
  def get(id: UUID)(implicit ec: ExecutionContext): ProcessResult[CardListQueryResult]
}

object CardListQuery {
  case class CardListQueryResult(
    id: UUID,
    title: String
  )
}
