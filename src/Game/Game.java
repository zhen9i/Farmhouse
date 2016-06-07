
package game;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import animal.AnimationConstant;
import animal.Barrel;
import animal.Cow;
import animal.Egg;
import animal.Han;
import animal.Milk;
import animal.Tiger;
import game.Statistic.StatisticItem;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * @author zhurko.e
 *
 */

public class Game extends Background implements AnimationConstant {

  private final static String backgroundimg = "/resources/Field.jpg";
  private final static String buyhan = "/resources/gethan.JPG";
  private final static String buycow = "/resources/getcow.JPG";
  private final static String buybot = "/resources/getbot.JPG";
  private final String SAVE_FILE = FileSaver
      .generateSaveFile(FileSaver.DIR_PATH_SAVE, FileSaver.EXT_FILE_DAT);
  private final String STAT_FILE = FileSaver
      .generateSaveFile(FileSaver.DIR_PATH_STAT, FileSaver.EXT_FILE_DAT);
  private final String NOT_FILE = FileSaver
      .generateSaveFile(FileSaver.NOT_PATH_STAT, FileSaver.TXT_FILE_NOT);

  private Label addhan;
  private Label addcow;
  private Label addbot;
  private Label money;
  private VBox vbox;
  private HBox cashbox;
  private LinkedList<Han> myhans;
  private LinkedList<Egg> eggs;
  private LinkedList<Cow> cows;
  private LinkedList<Milk> milk;

  private int cash = 100000;
  private boolean cheating = false;
  private int speed = 1;
  private boolean gamePlay = true;
  private Tiger tiger;
  private Barrel barrel;
  private StatisticItem statistics;

  private AudioClip sound =
      new AudioClip(getClass().getResource("/resources/hanmusic.mp3").toString());

  public Game(BorderPane pane, int screenW, int screenH) {
    super(pane, backgroundimg, screenW, screenH);
    myhans = new LinkedList<Han>();
    eggs = new LinkedList<Egg>();
    cows = new LinkedList<Cow>();
    milk = new LinkedList<Milk>();
    barrel = new Barrel(screenW, screenH);

    sound.play();

    initBuyHan();
    initBuyCow();
    initBuyBot();

    money = new Label();
    money.setFont(Font.font("Tahoma", 20));
    money.setTextFill(Color.GOLD);

    cashbox = new HBox();
    cashbox.setAlignment(Pos.CENTER_RIGHT);
    cashbox.getChildren().add(money);

    vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);
    vbox.getChildren().add(addhan);
    vbox.getChildren().add(addcow);
    vbox.getChildren().add(addbot);

    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        update();
        cheat();
        if (!sound.isPlaying()) {
          sound.play();
        }
      }
    };
    timer.start();
    draw();
  }

  private void initBuyHan() {
    addhan = new Label();
    addhan.setPrefSize(ICON, ICON);
    addhan.setBackground(new javafx.scene.layout.Background(
        new BackgroundImage(new Image(getClass().getResourceAsStream(buyhan)),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
    addhan.setOnMouseClicked(event -> {
      if (cash >= ItemConstant.HANPRICE
          && myhans.size() < ItemConstant.ANIMAL_COUNT) {
        Han han = new Han(WIDTH, HEIGHT);
        myhans.addLast(han);
        borderPane.getChildren().add(han);
        cash -= ItemConstant.HANPRICE;
        statistics = StatisticItem.ADD_HAN;
        saveStatistics(statistics);
      }
    });
  }

  private void initBuyCow() {
    addcow = new Label();
    addcow.setPrefSize(ICON, ICON);;
    addcow.setBackground(new javafx.scene.layout.Background(
        new BackgroundImage(new Image(getClass().getResourceAsStream(buycow)),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
    addcow.setOnMouseClicked(event -> {
      if (cash >= ItemConstant.COWPRICE
          && cows.size() < ItemConstant.ANIMAL_COUNT) {
        Cow cow = new Cow(WIDTH, HEIGHT);
        cows.addLast(cow);
        borderPane.getChildren().add(cow);
        cash -= ItemConstant.COWPRICE;
        this.statistics = StatisticItem.ADD_COW;
        saveStatistics(statistics);
      }
    });
  }

  private void initBuyBot() {
    addbot = new Label();
    addbot.setPrefSize(ICON, ICON);
    addbot.setBackground(new javafx.scene.layout.Background(
        new BackgroundImage(new Image(getClass().getResourceAsStream(buybot)),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
    addbot.setOnMouseClicked(event -> {
      if (!cheating) {
        cheating = true;
        tiger = new Tiger(WIDTH, HEIGHT);
        borderPane.getChildren().add(tiger);
      } else if (speed < SPEED_MAX) {
        speed++;
      }
    });
  }

  public boolean getGamePlay() {
    return gamePlay;
  }

  @Override
  public void draw() {
    super.draw();
    borderPane.setLeft(vbox);
    borderPane.setTop(cashbox);
    borderPane.getChildren().add(barrel);
  }

  public void update() {
    Iterator<Egg> itEgg = eggs.iterator();
    Iterator<Milk> itMilk = milk.iterator();
    Iterator<Han> itHan = myhans.iterator();
    Iterator<Cow> itCow = cows.iterator();

    while (itEgg.hasNext()) {
      Egg egg = itEgg.next();
      if (egg.isPickedUp()) {
        itEgg.remove();
        cash += ItemConstant.EGGPRICE;
        statistics = StatisticItem.PUT_EGG;
        saveStatistics(statistics);
      }
    }
    while (itMilk.hasNext()) {
      Milk tmilk = itMilk.next();
      if (tmilk.isPickedUp()) {
        itMilk.remove();
        cash += ItemConstant.MILKPRICE;
        statistics = StatisticItem.PUT_MILK;
        saveStatistics(statistics);
      }
    }
    while (itHan.hasNext()) {
      Han han = itHan.next();
      if (barrel.isEmpty()) {
        if (han.getHealthPoint() > 0) {
          han.setHealthPoint(han.getHealthPoint() - 1);
        } else {
          borderPane.getChildren().remove(han);
          itHan.remove();
          continue;
        }
      }
      han.walk();
      if (eggs.size() <= ItemConstant.ITEMCOUNT) {
        han.putEgg(borderPane, eggs);
      }
    }
    while (itCow.hasNext()) {
      Cow cow = itCow.next();
      if (barrel.isEmpty()) {
        if (cow.getHealthPoint() > 0) {
          cow.setHealthPoint(cow.getHealthPoint() - 1);
        } else {
          borderPane.getChildren().remove(cow);
          itCow.remove();
          continue;
        }
      }
      cow.walk();
      if (milk.size() <= ItemConstant.ITEMCOUNT) {
        cow.putMilk(borderPane, milk);
      }
    }
    money.setText("Cow: " + cows.size() + "/" + ItemConstant.ANIMAL_COUNT
        + " Han: " + myhans.size() + "/" + ItemConstant.ANIMAL_COUNT
        + " Cash: " + cash);
    saveReplay();
  }

  public void sortEgg(LinkedList<Egg> eggs) {
    eggs.sort(new Comparator<Egg>() {

      @Override
      public int compare(Egg o1, Egg o2) {
        if (getDistance(o1, tiger) > getDistance(o2, tiger)) {
          return 1;
        } else {
          return -1;
        }
      }
    });
  }

  public void sortMilk(LinkedList<Milk> milk) {
    milk.sort(new Comparator<Milk>() {

      @Override
      public int compare(Milk o1, Milk o2) {
        if (getDistance(o1, tiger) > getDistance(o2, tiger)) {
          return 1;
        } else {
          return -1;
        }
      }
    });
  }

  public double getDistance(Pane a, Pane b) {
    return Math.sqrt(Math.pow(a.getTranslateX() - b.getTranslateX(), 2)
        + Math.pow(a.getTranslateY() - b.getTranslateY(), 2));
  }

  @SuppressWarnings("unchecked")
  public void cheat() {
    if (cheating) {
      LinkedList<Egg> eatEgg = (LinkedList<Egg>) eggs.clone();
      LinkedList<Milk> eatMilk = (LinkedList<Milk>) milk.clone();
      for (int i = 0; i < speed; i++) {
        if (!eggs.isEmpty() && !milk.isEmpty()) {
          sortMilk(eatMilk);
          sortEgg(eatEgg);
          if (getDistance(eatEgg.getFirst(),
              tiger) < getDistance(eatMilk.getFirst(), tiger)) {
            if (tiger.take(eggs.getFirst().getTranslateX(),
                eggs.getFirst().getTranslateY())) {
              int index = eggs.indexOf(eatEgg.removeFirst());
              eggs.remove(index).pickUp();
              cash += ItemConstant.EGGPRICE;
            }
          } else {
            if (tiger.take(eatMilk.getFirst().getTranslateX(),
                eatMilk.getFirst().getTranslateY())) {
              int index = milk.indexOf(eatMilk.removeFirst());
              milk.remove(index).pickUp();
              cash += ItemConstant.MILKPRICE;
            }
          }
        } else if (!eggs.isEmpty()) {
          sortEgg(eatEgg);
          if (tiger.take(eatEgg.getFirst().getTranslateX(),
              eatEgg.getFirst().getTranslateY())) {
            int index = eggs.indexOf(eatEgg.removeFirst());
            eggs.remove(index).pickUp();
            cash += ItemConstant.EGGPRICE;
          }
        } else if (!milk.isEmpty()) {
          sortMilk(eatMilk);
          if (tiger.take(eatMilk.getFirst().getTranslateX(),
              eatMilk.getFirst().getTranslateY())) {
            int index = milk.indexOf(eatMilk.removeFirst());
            milk.remove(index).pickUp();
            cash += ItemConstant.MILKPRICE;
          }
        } else {
          tiger.walk();
        }
      }
    }
  }

  public void saveStatistics(StatisticItem item) {
    try (DataOutputStream oStream = new DataOutputStream(
        new BufferedOutputStream(new FileOutputStream(STAT_FILE, true)))) {
      oStream.writeInt(item.getValue());
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    transformStatistic(item);
  }

  public void transformStatistic(StatisticItem item) {
    try (DataOutputStream oStream = new DataOutputStream(
        new BufferedOutputStream(new FileOutputStream(NOT_FILE, true)))) {
      String notation = new Notation().statisticNotation(item);
      oStream.writeUTF(notation);
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  public void saveReplay() {
    Iterator<Han> itHan = myhans.iterator();
    Iterator<Cow> itCow = cows.iterator();
    Iterator<Egg> itEgg = eggs.iterator();
    Iterator<Milk> itMilk = milk.iterator();

    try (DataOutputStream oStream = new DataOutputStream(
        new BufferedOutputStream(new FileOutputStream(SAVE_FILE, true)))) {
      oStream.writeInt(myhans.size());
      oStream.writeInt(cows.size());
      oStream.writeInt(eggs.size());
      oStream.writeInt(milk.size());
      oStream.writeInt(barrel.getBarrelLevel());
      while (itHan.hasNext()) {
        Han han = itHan.next();
        han.save(oStream);
      }
      while (itCow.hasNext()) {
        Cow cow = itCow.next();
        cow.save(oStream);
      }
      while (itEgg.hasNext()) {
        Egg egg = itEgg.next();
        egg.save(oStream);
      }
      while (itMilk.hasNext()) {
        Milk aMilk = itMilk.next();
        aMilk.save(oStream);
      }
      oStream.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }
}
