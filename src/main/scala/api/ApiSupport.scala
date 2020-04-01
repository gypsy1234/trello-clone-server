package api

import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.server._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import lib.ErrorType
import lib.TypeAlias.ProcessResult

import scala.util._

trait ApiSupport
  extends Directives
    with FailFastCirceSupport {

  def response[T](
    f: => ProcessResult[T]
  )(g: T => Route)(
    implicit errorMapping: ErrorType => (StatusCode, String)
  ): Route =
    onComplete(f.value) {
      case Success(Right(r)) =>
        g(r)

      case Success(Left(errors)) =>
        val apiErrors = errors.map(errorMapping).toList
        complete(apiErrors.head)

      case Failure(e) =>
        complete((StatusCodes.InternalServerError, e.getMessage))
    }
}
