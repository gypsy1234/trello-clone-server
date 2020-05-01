package it.apitest.card_list

import api.CardListApi.CardListPostInput
import org.scalatest._
import org.scalatest.matchers.should._
import it.apitest.ApiClientSupport.{getCardList, postCardList, getCardLists}

class CardListTest extends FlatSpec with Matchers {

  behavior of "CardList"
  it should "カードリストを登録、取得できる" in {
    val id = postCardList(CardListPostInput("タイトル")).success(_.id)
    getCardList(id).success { out =>
      out.id shouldEqual id
      out.title shouldEqual "タイトル"
    }
  }

  it should "カードリストの一覧を取得できる" in {
    val id1 = postCardList(CardListPostInput("タイトル1")).success(_.id)
    val id2 = postCardList(CardListPostInput("タイトル2")).success(_.id)
    getCardLists.success { out =>

      val res1 = out.find(_.id == id1)
      res1.exists(_.title == "タイトル1") shouldEqual true

      val res2 = out.find(_.id == id2)
      res2.exists(_.title == "タイトル2") shouldEqual true

    }
  }
}
