package tables

import scalikejdbc._

object TableFixtures {
  lazy val cardList =
    sql"""
    CREATE TABLE `CardList` (
      `id` varchar(36) NOT NULL,
      `title` varchar(128) NOT NULL,
      PRIMARY KEY (`id`)
    )
    """
}
