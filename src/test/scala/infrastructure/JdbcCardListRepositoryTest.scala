package infrastructure

import java.util.UUID

import cats.data.EitherT
import domain.infrastructure.JdbcCardListRepository
import domain.model.{CardList, CardListId, CardListTitle}
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

class JdbcCardListRepositoryTest
  extends FlatSpec
    with Matchers
    with TestSupport
    with WithInmemoryDB {

  override protected val setupSqls: Seq[SQL[_,_]] =
    Seq(
      TestTableFixture.cardList
    )

  trait Fixtures extends BasicFixtures {
    implicit val dbOp: ScalikeJdbcOperation = autoDBOp
    val target = new JdbcCardListRepository
  }

  behavior of "JdbcCardListRepository"
  it should "登録、取得ができる" in new Fixtures {
    val list: CardList = CardList(CardListId(UUID.randomUUID()), CardListTitle("タイトル"))
    val result: EitherT[Future, Errors, CardList] =
      for {
        _ <- target.store(list)
        result <- target.get(list.id)
      } yield result
    awaitSuccess(result) shouldEqual list
  }
}
