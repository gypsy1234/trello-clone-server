package test_lib

import scalikejdbc._

object TestTableFixture {
  lazy val cardList =
    sql"""
    CREATE TABLE `CardList` (
      `id` varchar(36) NOT NULL,
      `title` varchar(128) NOT NULL,
      PRIMARY KEY (`id`)
    )
    """

  lazy val card =
    sql"""
    CREATE TABLE `Card` (
      `id` varchar(36) NOT NULL,
      `cardListId` varchar(36) NOT NULL,
      `title` varchar(128) NOT NULL,
      `position` double NOT NULL,
      PRIMARY KEY (`id`)
    )
    """
}
