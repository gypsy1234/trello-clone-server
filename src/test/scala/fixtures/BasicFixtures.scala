package fixtures

import lib.TypeAlias.ProcessResult
import lib.{DBContext, DBOperation, ScalikeJdbcOperation}
import scalikejdbc.AutoSession

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

trait BasicFixtures {

  implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  val autoDBCtx: DBContext = new DBContext {
    override def tx[T](f: DBOperation => Future[T]): Future[T] = f(autoDBOp)
    override def tx[T](f: DBOperation => ProcessResult[T]): ProcessResult[T] = f(autoDBOp)
    override def read[T](f: DBOperation => ProcessResult[T]): ProcessResult[T] = f(autoDBOp)
  }

  val autoDBOp: ScalikeJdbcOperation = ScalikeJdbcOperation(AutoSession)

}
