package domain.model

import java.util.UUID

case class Card(
  id: CardId,
  listId: CardListId,
  title: CardTitle,
  position: CardPosition
)

case class CardId(value: UUID) extends AnyVal
case class CardTitle(value: String) extends AnyVal
case class CardPosition(value: Double) extends AnyVal
