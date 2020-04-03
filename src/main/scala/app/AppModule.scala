package app

import app.command.{CardCommand, CardListCommand}
import app.query.{CardListQuery, CardQuery}
import domain.model.{Card, CardList}
import domain.service.{CardListService, CardService}
import lib.DBContext

import scala.concurrent.ExecutionContext

trait AppModule {
  import com.softwaremill.macwire._

  private implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  lazy val cardListCommand: CardListCommand = wire[CardListCommand]
  lazy val cardCommand: CardCommand = wire[CardCommand]

  def cardListQuery: CardListQuery
  def cardListService: CardListService[CardList]
  def cardQuery: CardQuery
  def cardService: CardService[Card]
  def dbContext: DBContext
}
