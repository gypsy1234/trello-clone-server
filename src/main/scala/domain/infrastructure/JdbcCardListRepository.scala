package domain.infrastructure

import java.util.UUID

import domain.model.{CardList, CardListId, CardListRepository, CardListTitle}
import lib.{DBOperation, ProcessResult, UseScalikeJdbc}
import lib.TypeAlias.ProcessResult
import cats.syntax.either._
import scalikejdbc._
import tables.CardListRecord
import lib.TypeConversion.err
import lib.ErrorType.{CardListNotFound, CardListPersistenceFailed}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

class JdbcCardListRepository
  extends CardListRepository
    with UseScalikeJdbc {

  import JdbcCardListRepository._

  override def store(list: CardList)(implicit dbOperation: DBOperation): ProcessResult[Unit] =
    ProcessResult {
      exec { implicit s =>
        Try {
          applyUpdate {
            delete.from(CardListRecord)
              .where.eq(clCols.id, list.id.value.toString)
          }
          applyUpdate {
            insert.into(CardListRecord).namedValues(
              clCols.id -> list.id.value.toString,
              clCols.title -> list.title.value
            )
          }
          ()
        }.toEither.leftMap { e => err(CardListPersistenceFailed(e))}
      }
    }

  override def get(listId: CardListId)(implicit dbOperation: DBOperation): ProcessResult[CardList] =
    ProcessResult {
      exec { implicit s =>
        val maybeRecord = withSQL {
          select.from(CardListRecord as cl)
            .where.eq(cl.id, listId.value.toString)
        }.map(CardListRecord(cl.resultName)).single.apply()

        val maybeCardList = maybeRecord.map { record =>
          CardList(
            id = CardListId(UUID.fromString(record.id)),
            title = CardListTitle(record.title)
          )
        }

        Either.fromOption(
          maybeCardList,
          err(CardListNotFound)
        )
      }
    }
}

object JdbcCardListRepository {
  lazy val cl: QuerySQLSyntaxProvider[SQLSyntaxSupport[CardListRecord], CardListRecord] = CardListRecord.syntax("cl")
  lazy val clCols: ColumnName[CardListRecord] = CardListRecord.column
}
