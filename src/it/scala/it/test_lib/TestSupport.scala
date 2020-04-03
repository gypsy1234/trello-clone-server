package it.test_lib

import cats.data.NonEmptyList
import lib.ErrorType
import lib.TypeAlias.ProcessResult

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait TestSupport {

  def awaitSuccess[T](f: => ProcessResult[T]): T =
    Await.result(f.value, Duration.Inf) match {
      case Right(r) => r
      case Left(e) => throw new IllegalArgumentException(e.toString)
    }

  def awaitError[T](f: => ProcessResult[T]): NonEmptyList[ErrorType] =
    Await.result(f.value, Duration.Inf) match {
      case Left(es) => es
      case _ => throw new IllegalArgumentException()
    }

  def await[T](f: Future[T]): T =
    Await.result(f, Duration.Inf)
}
