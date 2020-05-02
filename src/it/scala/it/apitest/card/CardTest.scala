package it.apitest.card

import api.CardApi.CardPostInput
import api.CardListApi.CardListPostInput
import org.scalatest._
import org.scalatest.matchers.should._
import it.apitest.ApiClientSupport.{postCard, postCardList, getCard}

class CardTest extends FlatSpec with Matchers {

  behavior of "CardList"
  it should "カードを登録、取得できる" in {
    val listId = postCardList(CardListPostInput("リストタイトル")).success(_.id)
    val id = postCard(listId, CardPostInput("タイトル")).success(_.id)
    getCard(id).success { out =>
      out.id shouldEqual id
      out.listId shouldEqual listId
      out.listTitle shouldEqual "リストタイトル"
      out.title shouldEqual "タイトル"
    }
  }
}
