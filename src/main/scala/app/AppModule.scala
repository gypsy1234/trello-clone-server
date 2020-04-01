package app

import app.command.CardListCommand
import app.query.CardListQuery
import domain.model.CardList
import domain.sercice.CardListService
import lib.DBContext

import scala.concurrent.ExecutionContext

trait AppModule {
  import com.softwaremill.macwire._

  private implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  lazy val cardListCommand: CardListCommand = wire[CardListCommand]

  def cardListQuery: CardListQuery
  def cardListService: CardListService[CardList]
  def dbContext: DBContext
}
