package it.apitest

import java.util.UUID

import api.CardApi.{CardPostInput, CardPostOutput, CardPutInput}
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

  def getCardLists: ApiTestRequest[_, Seq[CardListQueryResult]] =
    ApiTestRequest.get(s"/v1/card-lists")

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

  def putCard(
    id: UUID,
    input: CardPutInput
  ): ApiTestRequest[_, Unit] =
    ApiTestRequest.put[CardPutInput, Unit](
      path = s"/v1/cards/${id.toString}",
      input = input
    )

  def getCards: ApiTestRequest[_, Seq[CardQueryResult]] =
    ApiTestRequest.get(s"/v1/cards")

  def getCards(
     id: UUID
   ): ApiTestRequest[_, Seq[CardQueryResult]] =
    ApiTestRequest.get(s"/v1/card-lists/${id.toString}/cards")

  def getCard(
    id: UUID
  ): ApiTestRequest[_, CardQueryResult] =
    ApiTestRequest.get(s"/v1/cards/${id.toString}")
}
