package lib

trait ErrorType

object ErrorType {
  case object CardListNotFound extends ErrorType
  case class CardListPersistenceFailed(e: Throwable) extends ErrorType
}
