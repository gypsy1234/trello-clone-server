package lib

import cats.data.NonEmptyList

object TypeConversion {
  def err[T](f: => ErrorType): NonEmptyList[ErrorType] =
    NonEmptyList.of(f)
}