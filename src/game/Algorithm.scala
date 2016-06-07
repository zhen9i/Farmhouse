package game

import game.FileSaver.ReplayInfo

class Algorithm {
  def sort(array: Array[ReplayInfo]) {
    def swap(a: Int, b: Int) {
      val temp = array(a);
      array(a) = array(b)
      array(b) = temp
    }
    def mySort(begin: Int, end: Int) {
      val temp = array((begin + end) / 2).getLenght
      var i = begin
      var j = end
      while (i <= j) {
        while (array(i).getLenght() > temp) {
          i += 1
        }
        while (array(j).getLenght() < temp) {
          j -= 1
        }
        if (i <= j) {
          swap(i, j)
          i += 1
          j -= 1
        }
      }
      if (begin < j) mySort(begin, j)
      if (j < end) mySort(i, end)
    }
    mySort(0, array.length - 1)
  }
  
  def countCows(array: Array[Integer]): Int =
  {
    array.filter(_ == Statistic.StatisticItem.ADD_COW.getValue).size
  }
  def countHans(array: Array[Integer]): Int =
  {
    array.filter(_ == Statistic.StatisticItem.ADD_HAN.getValue).size
  }
  def countEggs(array: Array[Integer]): Int =
  {
    array.filter(_ == Statistic.StatisticItem.PUT_EGG.getValue).size
  }
  def countMilk(array: Array[Integer]): Int =
  {
    array.filter(_ == Statistic.StatisticItem.PUT_MILK.getValue).size
  }
}