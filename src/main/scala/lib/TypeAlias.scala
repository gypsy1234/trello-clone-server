package lib

import cats.data.{EitherT, NonEmptyList}
import cats.syntax.either._

import scala.concurrent.{ExecutionContext, Future}

object TypeAlias {

  type Errors = NonEmptyList[ErrorType]

  type ProcessResult[T] = EitherT[Future, Errors, T]

  type MaybeErrors[T] = Either[Errors, T]

}

object ProcessResult {

  import TypeAlias._

  def apply[T](f: => MaybeErrors[T])(implicit ec: ExecutionContext): ProcessResult[T] =
    EitherT {
      Future {
        f
      }
    }

  def success[T](f: => T)(implicit ec: ExecutionContext): ProcessResult[T] =
    EitherT {
      Future {
        f.asRight
      }
    }

  def wrap[T](f: => Either[ErrorType, T]): ProcessResult[T] =
    EitherT {
      Future.successful {
        f match {
          case Right(r) => r.asRight
          case Left(e) => NonEmptyList.of(e).asLeft
        }
      }
    }
}