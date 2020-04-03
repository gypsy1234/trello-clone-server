package infrastructure

import app.query.{CardListQuery, CardQuery}
import domain.infrastructure.{JdbcCardListRepository, JdbcCardRepository}
import domain.model.{CardListRepository, CardRepository}
import lib.{DBContext, ScalikeJdbcContext}

import scala.concurrent.ExecutionContext.Implicits.global

trait InfrastructureModule {
  import com.softwaremill.macwire._
  lazy val dbContext: DBContext = new ScalikeJdbcContext()
  lazy val cardListRepository: CardListRepository = wire[JdbcCardListRepository]
  lazy val cardListQuery: CardListQuery = wire[JdbcCardListQuery]
  lazy val cardRepository: CardRepository = wire[JdbcCardRepository]
  lazy val cardQuery: CardQuery = wire[JdbcCardQuery]
}
