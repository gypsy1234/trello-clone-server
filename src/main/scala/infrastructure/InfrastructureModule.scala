package infrastructure

import app.query.CardListQuery
import domain.infrastructure.JdbcCardListRepository
import domain.model.CardListRepository
import lib.{DBContext, ScalikeJdbcContext}

import scala.concurrent.ExecutionContext.Implicits.global

trait InfrastructureModule {
  import com.softwaremill.macwire._
  lazy val dbContext: DBContext = new ScalikeJdbcContext()
  lazy val cardListRepository: CardListRepository = wire[JdbcCardListRepository]
  lazy val cardListQuery: CardListQuery = wire[JdbcCardListQuery]
}
