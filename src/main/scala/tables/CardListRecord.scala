package tables

import scalikejdbc._

case class CardListRecord(
  id: String,
  title: String
)

object CardListRecord extends SQLSyntaxSupport[CardListRecord] {

  override val tableName = "CardList"

  override val columns = Seq("id", "title")

  def apply(cl: ResultName[CardListRecord])(rs: WrappedResultSet): CardListRecord = autoConstruct(rs, cl)
}