package game;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GameStatistic {
  private String id;
  private int countHan;
  private int countCow;
  private int countEgg;
  private int countMilk;

  public GameStatistic(DataInputStream iStream, String aId) {
    id = aId;
    ArrayList<Integer> action = new ArrayList<>();
    try {
      while (iStream.available() > 0) {
        action.add(iStream.readInt());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    Integer[] array = new Integer[action.size()];
    array = action.toArray(array);
    countHan = new Algorithm().countHans(array);
    countCow = new Algorithm().countCows(array);
    countEgg = new Algorithm().countEggs(array);
    countMilk = new Algorithm().countMilk(array);
  }

  public GameStatistic(String aId, int hans, int cows, int eggs, int milk) {
    id = aId;
    countHan = hans;
    countCow = cows;
    countEgg = eggs;
    countMilk = milk;
  }

  public int getCountHan() {
    return countHan;
  }

  public int getCountCow() {
    return countCow;
  }

  public int getCountEgg() {
    return countEgg;
  }

  public int getCountMilk() {
    return countMilk;
  }

  public String getId() {
    return id;
  }
}
