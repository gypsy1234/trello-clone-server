package it.apitest.card_list

import java.util.UUID

import api.CardListApi.{CardListPostInput, CardListPostOutput}
import app.query.CardListQuery.CardListQueryResult
import it.test_lib.ApiTestRequest
import org.scalatest._
import org.scalatest.matchers.should._
import io.circe.Decoder._
import io.circe.Encoder._
import it.test_lib.TestRestClient._

class CardListTest extends FlatSpec with Matchers {

  behavior of "CardList"
  it should "カードリストを登録、取得できる" in {
    val id = postCardList(CardListPostInput("タイトル")).success(_.id)
    getCardList(id).success { out =>
      out.id shouldEqual id
      out.title shouldEqual "タイトル"
    }
  }

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

}
