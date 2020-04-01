package domain.model

import java.util.UUID

case class CardList(
  id: CardListId,
  title: CardListTitle
)

case class CardListId(value: UUID) extends AnyVal
case class CardListTitle(value: String) extends AnyVal
