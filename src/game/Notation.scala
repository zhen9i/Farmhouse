package game

import game.Statistic.StatisticItem

class Notation {
  def statisticNotation(value : StatisticItem): String = {
    value match {
      case StatisticItem.ADD_COW => "Player bought cow\n"
      case StatisticItem.ADD_HAN => "Player bought han\n"
      case StatisticItem.PUT_EGG => "Hen laid an egg\n"
      case StatisticItem.PUT_MILK => "Cow gave milk\n"
    }
  }
}
