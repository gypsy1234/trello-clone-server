import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.`Access-Control-Allow-Origin`
import akka.http.scaladsl.server.{ExceptionHandler, HttpApp, RejectionHandler, Route}
import api.{CardApi, CardListApi}
import app.AppModule
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import domain.DomainModule
import infrastructure.InfrastructureModule
import lib.UseDB
import scalikejdbc.config.DBs
import scalikejdbc.{AutoSession, DBSession, SQL}
import tables.Tables

import scala.concurrent.ExecutionContext.Implicits.global

object Main
  extends UseDB {
  def main(args: Array[String]): Unit = {

    val setupSqls: Seq[SQL[_, _]] =
      Seq(
        Tables.cardList,
        Tables.card
      )

    def setupSqlsBySession: Map[DBSession, Seq[SQL[_, _]]] = Map(AutoSession -> setupSqls)

    import scalikejdbc._
    Class.forName("org.h2.Driver")
    ConnectionPool.singleton("jdbc:h2:mem:db", "username", "password",
      ConnectionPoolSettings(initialSize = 20, maxSize = 50))

    GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
      enabled = true,
      singleLineMode = true,
      printUnprocessedStackTrace = true,
      stackTraceDepth = 15,
      logLevel = Symbol("info"),
      warningEnabled = true,
      warningThresholdMillis = 1000L,
      warningLogLevel = Symbol("warn"),
      maxColumnSize = Some(100),
      maxBatchParamSize = Some(20)
    )

    DBs.setupAll()

    setupSqlsBySession.foreach { case (session, sql) =>
      execSql(sql: _*)(session)
    }

    WebServer.startServer("0.0.0.0", 9000)
  }
}

object WebServer
  extends HttpApp {

  override protected def routes: Route = {
    val module =
      new AppModule
        with InfrastructureModule
        with DomainModule

    val route: Route = {
      val exceptionHandler = ExceptionHandler {
        case e: Exception =>
          complete((StatusCodes.InternalServerError, e.getMessage))
      }

      val rejectionHandler = corsRejectionHandler.withFallback(RejectionHandler.default)

      val handleErrors = handleRejections(rejectionHandler) & handleExceptions(exceptionHandler)

      val route = {
        val mainRoutes: Route = {
          new CardListApi(module).routes ~
          new CardApi(module).routes
        }

        handleErrors {
          cors() {
            handleErrors {
              extractRequestContext { ctx =>
                mainRoutes
              }
            }
          }
        }
      }
      route
    }
    route
  }
}
