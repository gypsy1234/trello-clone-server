package test_lib

import lib.UseDB
import org.scalatest._
import scalikejdbc._

trait WithInmemoryDB extends SuiteMixin with UseDB { this: Suite =>

  abstract override protected def runTest(testName: String, args: Args): Status = {
    Class.forName("org.h2.Driver")
    ConnectionPool.singleton(s"jdbc:h2:mem:test;MODE=MYSQL", "user", "pass")

    GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
      enabled = true,
      singleLineMode = true,
      printUnprocessedStackTrace = false,
      stackTraceDepth = 15,
      logLevel = Symbol("debug"),
      warningEnabled = true,
      warningThresholdMillis = 3000L,
      warningLogLevel = Symbol("warn"),
      maxColumnSize = Some(100),
      maxBatchParamSize = Some(20)
    )

    try {
      setupSqlsBySession.foreach { case (session, sql) =>
        execSql(sql: _*)(session)
      }
      super.runTest(testName, args)
    } finally {
      ConnectionPool.closeAll()
    }
  }

  protected val setupSqls: Seq[SQL[_,_]] = Seq.empty

  protected def setupSqlsBySession: Map[DBSession, Seq[SQL[_,_]]] =
    Map(AutoSession -> setupSqls)
}
