package domain

import domain.model.{Card, CardList, CardListRepository, CardRepository}
import domain.service.{CardListService, CardService}
import domain.service.interpreter.{CardListServiceInterpreter, CardServiceInterpreter}

trait DomainModule {
  import com.softwaremill.macwire._
  lazy val cardListService: CardListService[CardList] = wire[CardListServiceInterpreter]
  lazy val cardService: CardService[Card] = wire[CardServiceInterpreter]
  def cardListRepository: CardListRepository
  def cardRepository: CardRepository
}
