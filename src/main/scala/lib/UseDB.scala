package lib

import scalikejdbc._

trait UseDB {

  def execSql(sqls: SQL[_,_]*)(implicit s: DBSession = AutoSession): Unit = {
    sqls.foreach { sql =>
      sql.execute.apply()
    }
  }
  def execSql(sqls: Map[DBSession, Seq[SQL[_,_]]]): Unit = {
    sqls.foreach { case (session, sql) =>
      execSql(sql: _*)(session)
    }
  }

}

object UseDB
