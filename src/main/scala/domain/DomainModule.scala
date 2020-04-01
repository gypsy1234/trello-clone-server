package domain

import domain.model.{CardList, CardListRepository}
import domain.sercice.CardListService
import domain.sercice.interpreter.CardListServiceInterpreter

trait DomainModule {
  import com.softwaremill.macwire._
  lazy val cardListService: CardListService[CardList] = wire[CardListServiceInterpreter]
  def cardListRepository: CardListRepository
}
