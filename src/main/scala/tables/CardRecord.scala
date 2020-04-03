package tables

import scalikejdbc._

case class CardRecord(
  id: String,
  cardlistid: String,
  title: String
)

object CardRecord extends SQLSyntaxSupport[CardRecord] {

  override val tableName = "Card"

  override val columns = Seq("id", "cardListId", "title")

  def apply(c: ResultName[CardRecord])(rs: WrappedResultSet): CardRecord = autoConstruct(rs, c)
}
