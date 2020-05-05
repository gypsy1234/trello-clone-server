package domain.infrastructure

import java.util.UUID

import domain.model.{Card, CardId, CardListId, CardPosition, CardRepository, CardTitle}
import lib.{DBOperation, ProcessResult, UseScalikeJdbc}
import lib.TypeAlias.ProcessResult
import cats.syntax.either._
import scalikejdbc._
import tables.CardRecord
import lib.TypeConversion.err
import lib.ErrorType.{CardNotFound, CardPersistenceFailed}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

class JdbcCardRepository
  extends CardRepository
    with UseScalikeJdbc {

  import JdbcCardRepository._

  override def store(card: Card)(implicit dbOperation: DBOperation): ProcessResult[Unit] =
    ProcessResult {
      exec { implicit s =>
        Try {
          applyUpdate {
            delete.from(CardRecord)
              .where.eq(cCols.id, card.id.value.toString)
          }
          applyUpdate {
            insert.into(CardRecord).namedValues(
              cCols.id -> card.id.value.toString,
              cCols.cardlistid -> card.listId.value.toString,
              cCols.title -> card.title.value,
              cCols.position -> card.position.value
            )
          }
          ()
        }.toEither.leftMap { e => err(CardPersistenceFailed(e))}
      }
    }

  override def get(cardId: CardId)(implicit dbOperation: DBOperation): ProcessResult[Card] =
    ProcessResult {
      exec { implicit s =>
        val maybeRecord = withSQL {
          select.from(CardRecord as c)
            .where.eq(c.id, cardId.value.toString)
        }.map(CardRecord(c.resultName)).single.apply()

        val maybeCard = maybeRecord.map { record =>
          Card(
            id = CardId(UUID.fromString(record.id)),
            listId = CardListId(UUID.fromString(record.cardlistid)),
            title = CardTitle(record.title),
            position = CardPosition(record.position)
          )
        }

        Either.fromOption(
          maybeCard,
          err(CardNotFound)
        )
      }
    }
}

object JdbcCardRepository {
  lazy val c: QuerySQLSyntaxProvider[SQLSyntaxSupport[CardRecord], CardRecord] = CardRecord.syntax("c")
  lazy val cCols: ColumnName[CardRecord] = CardRecord.column
}
