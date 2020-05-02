package it.apitest.card

import api.CardApi.CardPostInput
import api.CardListApi.CardListPostInput
import org.scalatest._
import org.scalatest.matchers.should._
import it.apitest.ApiClientSupport.{postCard, postCardList, getCard, getCards}

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

  it should "カード一覧を取得できる" in {
    val cardListId1 = postCardList(CardListPostInput("リストタイトル1")).success(_.id)
    val cardListId2 = postCardList(CardListPostInput("リストタイトル2")).success(_.id)
    val id1 = postCard(cardListId1, CardPostInput("タイトル1")).success(_.id)
    val id2 = postCard(cardListId2, CardPostInput("タイトル2")).success(_.id)
    getCards.success { out =>

      val res1 = out.find(_.id == id1)
      res1.exists(_.title == "タイトル1") shouldEqual true

      val res2 = out.find(_.id == id2)
      res2.exists(_.title == "タイトル2") shouldEqual true

    }
  }

  it should "カードリストに紐づくカード一覧を取得できる" in {
    val cardListId = postCardList(CardListPostInput("リストタイトル")).success(_.id)
    val id1 = postCard(cardListId, CardPostInput("タイトル1")).success(_.id)
    val id2 = postCard(cardListId, CardPostInput("タイトル2")).success(_.id)
    getCards(cardListId).success { out =>

      val res1 = out.find(_.id == id1)
      res1.exists(_.title == "タイトル1") shouldEqual true

      val res2 = out.find(_.id == id2)
      res2.exists(_.title == "タイトル2") shouldEqual true

    }
  }
}
