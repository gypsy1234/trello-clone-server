package api

import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import lib.ErrorType
import lib.ErrorType.{CardListNotFound, CardListPersistenceFailed}

trait ApiErrorMapper {

  implicit val errorMapping: ErrorType => (StatusCode, String) = {
    case CardListNotFound =>
      (StatusCodes.NotFound, "指定されたカードリストが見つかりませんでした")
    case CardListPersistenceFailed(e) =>
      (StatusCodes.InternalServerError, "カードリストの登録が失敗しました" + e.getMessage)
  }
}

object ApiErrorMapper extends ApiErrorMapper
