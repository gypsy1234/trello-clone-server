package lib

import TypeAlias.ProcessResult
import scalikejdbc.ReadOnlyAutoSession

import scala.concurrent.Future

trait DBContext {
  def tx[T](f: DBOperation => Future[T]): Future[T]
  def tx[T](f: DBOperation => ProcessResult[T]): ProcessResult[T]
  def read[T](f: DBOperation => ProcessResult[T]): ProcessResult[T]
}

trait DBOperation

object DBOperation{
  lazy val defaultReadOnlyOperation: ScalikeJdbcOperation = ScalikeJdbcOperation(ReadOnlyAutoSession)
}