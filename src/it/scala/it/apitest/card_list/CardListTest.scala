package it.apitest.card_list

import api.CardListApi.CardListPostInput
import org.scalatest._
import org.scalatest.matchers.should._
import it.apitest.ApiClientSupport.{getCardList, postCardList}

class CardListTest extends FlatSpec with Matchers {

  behavior of "CardList"
  it should "カードリストを登録、取得できる" in {
    val id = postCardList(CardListPostInput("タイトル")).success(_.id)
    getCardList(id).success { out =>
      out.id shouldEqual id
      out.title shouldEqual "タイトル"
    }
  }
}
