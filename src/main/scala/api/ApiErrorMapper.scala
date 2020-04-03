package api

import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import lib.ErrorType
import lib.ErrorType.{CardListNotFound, CardListPersistenceFailed, CardNotFound, CardPersistenceFailed}

trait ApiErrorMapper {

  implicit val errorMapping: ErrorType => (StatusCode, String) = {
    case CardListNotFound =>
      (StatusCodes.NotFound, "指定されたカードリストが見つかりませんでした")
    case CardListPersistenceFailed(e) =>
      (StatusCodes.InternalServerError, "カードリストの登録が失敗しました" + e.getMessage)
    case CardNotFound =>
      (StatusCodes.NotFound, "指定されたカードが見つかりませんでした")
    case CardPersistenceFailed(e) =>
      (StatusCodes.InternalServerError, "カードの登録が失敗しました" + e.getMessage)
  }
}

object ApiErrorMapper extends ApiErrorMapper
