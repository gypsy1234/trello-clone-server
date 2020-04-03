package it.apitest

import java.util.UUID

import api.CardApi.{CardPostInput, CardPostOutput}
import api.CardListApi.{CardListPostInput, CardListPostOutput}
import app.query.CardListQuery.CardListQueryResult
import app.query.CardQuery.CardQueryResult
import it.test_lib.ApiTestRequest
import io.circe.Decoder._
import io.circe.Encoder._
import it.test_lib.TestRestClient._

object ApiClientSupport {

  def postCardList(
    input: CardListPostInput
  ): ApiTestRequest[_, CardListPostOutput] =
    ApiTestRequest.post[CardListPostInput, CardListPostOutput](
      path = s"/v1/card-lists",
      input = input
    )

  def getCardList(
    id: UUID
  ): ApiTestRequest[_, CardListQueryResult] =
    ApiTestRequest.get(s"/v1/card-lists/${id.toString}")

  def postCard(
    listId: UUID,
    input: CardPostInput
  ): ApiTestRequest[_, CardPostOutput] =
    ApiTestRequest.post[CardPostInput, CardPostOutput](
      path = s"/v1/card-lists/${listId.toString}/cards",
      input = input
    )

  def getCard(
    id: UUID
  ): ApiTestRequest[_, CardQueryResult] =
    ApiTestRequest.get(s"/v1/cards/${id.toString}")
}
