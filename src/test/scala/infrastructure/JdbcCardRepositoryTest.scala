package infrastructure

import java.util.UUID

import cats.data.EitherT
import domain.infrastructure.JdbcCardRepository
import domain.model.{Card, CardId, CardListId, CardTitle}
import org.scalatest._
import org.scalatest.matchers.should._

import scala.concurrent.Future
import cats.instances.future._
import fixtures.BasicFixtures
import it.test_lib.TestSupport
import lib.ScalikeJdbcOperation
import lib.TypeAlias.Errors
import scalikejdbc.SQL
import test_lib.{TestTableFixture, WithInmemoryDB}

class JdbcCardRepositoryTest
  extends FlatSpec
    with TestSupport
    with Matchers
    with WithInmemoryDB {

  override protected val setupSqls: Seq[SQL[_,_]] =
    Seq(
      TestTableFixture.card
    )

  trait Fixtures extends BasicFixtures {
    implicit val dbOp: ScalikeJdbcOperation = autoDBOp
    val target = new JdbcCardRepository
  }

  behavior of "JdbcCardRepository"
  it should "登録、取得ができる" in new Fixtures {
    val card: Card = Card(CardId(UUID.randomUUID()), CardListId(UUID.randomUUID()), CardTitle("タイトル"))
    val result: EitherT[Future, Errors, Card] =
      for {
        _ <- target.store(card)
        result <- target.get(card.id)
      } yield result
    awaitSuccess(result) shouldEqual card
  }
}
