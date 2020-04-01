package infrastructure

import java.util.UUID

import cats.syntax.either._
import app.query.CardListQuery
import app.query.CardListQuery.CardListQueryResult
import lib.ErrorType.CardListNotFound
import lib.{DBContext, ProcessResult, UseScalikeJdbc}
import lib.TypeAlias.ProcessResult
import lib.TypeConversion.err
import scalikejdbc._
import tables.CardListRecord

import scala.concurrent.ExecutionContext

class JdbcCardListQuery
  extends CardListQuery
    with UseScalikeJdbc {

  import JdbcCardListQuery._

  override def get(id: UUID)(implicit ec: ExecutionContext): ProcessResult[CardListQueryResult] =
    ProcessResult {
      DB readOnly { implicit s =>
        val result =
          withSQL {
            select
              .from(CardListRecord as cl)
              .where
              .eq(cl.id, id.toString)
          }.map { rs => CardListRecord(cl.resultName)(rs) }.single.apply()

        Either.fromOption(
          result.map{ r =>
            CardListQueryResult(
              id = UUID.fromString(r.id),
              title = r.title
            )
          },
          err(CardListNotFound)
        )
      }
    }
}

object JdbcCardListQuery {
  val cl: QuerySQLSyntaxProvider[SQLSyntaxSupport[CardListRecord], CardListRecord] = CardListRecord.syntax("cl")
}