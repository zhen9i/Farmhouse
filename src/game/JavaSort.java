package game;

import game.FileSaver.ReplayInfo;

public class JavaSort {

  public static ReplayInfo[] quickSort(ReplayInfo[] replayArray, int size) {
    int startIndex = 0;
    int endIndex = size - 1;
    doSort(replayArray, startIndex, endIndex);
    return replayArray;
  }

  private static ReplayInfo[] doSort(ReplayInfo[] replayArray, int start, int end) {
    if (start >= end)
      return replayArray;
    int i = start, j = end;
    int cur = i - (i - j) / 2;
    while (i < j) {
      while (i < cur
          && (replayArray[i].getLenght() <= replayArray[cur].getLenght())) {
        i++;
      }
      while (j > cur
          && (replayArray[cur].getLenght() <= replayArray[j].getLenght())) {
        j--;
      }
      if (i < j) {
        ReplayInfo temp = replayArray[i];
        replayArray[i] = replayArray[j];
        replayArray[j] = temp;
        if (i == cur)
          cur = j;
        else if (j == cur)
          cur = i;
      }
    }
    doSort(replayArray, start, cur);
    doSort(replayArray, cur + 1, end);
    return replayArray;
  }
}
