package infrastructure

import java.util.UUID

import cats.syntax.either._
import app.query.CardQuery
import app.query.CardQuery.CardQueryResult
import lib.ErrorType.CardListNotFound
import lib.{ProcessResult, UseScalikeJdbc}
import lib.TypeAlias.ProcessResult
import lib.TypeConversion.err
import scalikejdbc._
import tables.{CardListRecord, CardRecord}

import scala.concurrent.ExecutionContext

class JdbcCardQuery
  extends CardQuery
    with UseScalikeJdbc {

  import JdbcCardQuery._

  override def get(id: UUID)(implicit ec: ExecutionContext): ProcessResult[CardQueryResult] =
    ProcessResult {
      DB readOnly { implicit s =>
        val result =
          withSQL {
            select
              .from(CardRecord as c)
              .leftJoin(CardListRecord as cl)
                .on(c.cardlistid, cl.id)
              .where
                .eq(c.id, id.toString)
          }.map { rs =>
            CardQueryResult(
              id = UUID.fromString(rs.string(c.resultName.id)),
              listId = UUID.fromString(rs.string(cl.resultName.id)),
              listTitle = rs.string(cl.resultName.title),
              title = rs.string(c.resultName.title),
            )
          }.single.apply()

        Either.fromOption(
          result,
          err(CardListNotFound)
        )
      }
    }
}

object JdbcCardQuery {
  val c: QuerySQLSyntaxProvider[SQLSyntaxSupport[CardRecord], CardRecord] = CardRecord.syntax("c")
  val cl: QuerySQLSyntaxProvider[SQLSyntaxSupport[CardListRecord], CardListRecord] = CardListRecord.syntax("cl")
}
