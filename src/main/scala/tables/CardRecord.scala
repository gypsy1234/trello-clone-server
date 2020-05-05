package tables

import scalikejdbc._

case class CardRecord(
  id: String,
  cardlistid: String,
  title: String,
  position: Double
)

object CardRecord extends SQLSyntaxSupport[CardRecord] {

  override val tableName = "Card"

  override val columns = Seq("id", "cardListId", "title", "position")

  def apply(c: ResultName[CardRecord])(rs: WrappedResultSet): CardRecord = autoConstruct(rs, c)
}
