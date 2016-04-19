
package game;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import animal.Barrel;
import animal.Cow;
import animal.Egg;
import animal.Han;
import animal.Milk;
import animal.Tiger;
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

public class Game extends Background {

  private final static String backgroundimg = "Field.jpg";
  private final static String buyhan = "gethan.JPG";
  private final static String buycow = "getcow.JPG";
  private final static String buybot = "getbot.JPG";
  private final static String save = "save.dat";
  private final int ICON = 64;
  private final int ANIMAL_COUNT = 10;
  private final int SPEED_MAX = 7;

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
  private Barrel barrel;
  private int cash = 100000;
  private final int EGGPRICE = 50;
  private final int HANPRICE = 100;
  private final int MILKPRICE = 250;
  private final int COWPRICE = 5000;
  private final int ITEMCOUNT = 20;
  private boolean cheating = false;
  private int speed = 1;
  private int skipLenght = 0;

  private boolean gamePlay = true;
  private Tiger tiger;

  private AudioClip sound =
      new AudioClip(getClass().getResource("hanmusic.mp3").toString());

  public Game(BorderPane pane, int screenW, int screenH, boolean replay) {
    super(pane, backgroundimg, screenW, screenH);
    myhans = new LinkedList<Han>();
    eggs = new LinkedList<Egg>();
    cows = new LinkedList<Cow>();
    milk = new LinkedList<Milk>();
    barrel = new Barrel(screenW, screenH);

    sound.play();

    addhan = new Label();
    addhan.setPrefSize(ICON, ICON);
    addhan.setBackground(new javafx.scene.layout.Background(
        new BackgroundImage(new Image(getClass().getResourceAsStream(buyhan)),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
    addhan.setOnMouseClicked(event -> {
      if (cash >= HANPRICE && myhans.size() < ANIMAL_COUNT) {
        Han han = new Han(WIDTH, HEIGHT);
        myhans.addLast(han);
        borderPane.getChildren().add(han);
        cash -= HANPRICE;
      }
    });

    addcow = new Label();
    addcow.setPrefSize(ICON, ICON);;
    addcow.setBackground(new javafx.scene.layout.Background(
        new BackgroundImage(new Image(getClass().getResourceAsStream(buycow)),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
    addcow.setOnMouseClicked(event -> {
      if (cash >= COWPRICE && cows.size() < ANIMAL_COUNT) {
        Cow cow = new Cow(WIDTH, HEIGHT);
        cows.addLast(cow);
        borderPane.getChildren().add(cow);
        cash -= COWPRICE;
      }
    });

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

    if (replay) {
      addcow.setDisable(true);
      addhan.setDisable(true);
      addbot.setDisable(true);
    } else {
      try (DataOutputStream oStream = new DataOutputStream(
          new BufferedOutputStream(new FileOutputStream(save)))) {
        oStream.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }

    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        if (replay) {
          if (!playReplay()) {
            sound.stop();
            super.stop();
            gamePlay = false;
          }
        } else {
          update();
          cheat();
        }
        if (!sound.isPlaying()) {
          sound.play();
        }
      }
    };
    timer.start();
    draw();
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
        cash += EGGPRICE;
      }
    }
    while (itMilk.hasNext()) {
      Milk tmilk = itMilk.next();
      if (tmilk.isPickedUp()) {
        itMilk.remove();
        cash += MILKPRICE;
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
      if (eggs.size() <= ITEMCOUNT) {
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
      if (milk.size() <= ITEMCOUNT) {
        cow.putMilk(borderPane, milk);
      }
    }
    money.setText("Cow: " + cows.size() + "/" + ANIMAL_COUNT + " Han: "
        + myhans.size() + "/" + ANIMAL_COUNT + " Cash: " + cash);
    saveReplay();
  }

  public void sortEgg() {
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

  public void sortMilk() {
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

  public void cheat() {
    if (cheating) {
      for (int i = 0; i < speed; i++) {
        if (!eggs.isEmpty() && !milk.isEmpty()) {
          sortMilk();
          sortEgg();
          if (getDistance(eggs.getFirst(),
              tiger) < getDistance(milk.getFirst(), tiger)) {
            if (tiger.take(eggs.getFirst().getTranslateX(),
                eggs.getFirst().getTranslateY())) {
              eggs.removeFirst().pickUp();
              cash += EGGPRICE;
            }
          } else {
            if (tiger.take(milk.getFirst().getTranslateX(),
                milk.getFirst().getTranslateY())) {
              milk.removeFirst().pickUp();
              cash += MILKPRICE;
            }
          }
        } else if (!eggs.isEmpty()) {
          sortEgg();
          if (tiger.take(eggs.getFirst().getTranslateX(),
              eggs.getFirst().getTranslateY())) {
            eggs.removeFirst().pickUp();
            cash += EGGPRICE;
          }
        } else if (!milk.isEmpty()) {
          sortMilk();
          if (tiger.take(milk.getFirst().getTranslateX(),
              milk.getFirst().getTranslateY())) {
            milk.removeFirst().pickUp();
            cash += MILKPRICE;
          }
        } else {
          tiger.walk();
        }
      }
    }
  }

  public void saveReplay() {
    Iterator<Han> itHan = myhans.iterator();
    Iterator<Cow> itCow = cows.iterator();
    Iterator<Egg> itEgg = eggs.iterator();
    Iterator<Milk> itMilk = milk.iterator();

    try (DataOutputStream oStream = new DataOutputStream(
        new BufferedOutputStream(new FileOutputStream(save, true)))) {
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
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  public void updateHans(DataInputStream iStream, int hansCount) {
    Iterator<Han> itHan = myhans.iterator();
    try {
      while (itHan.hasNext()) {
        Han han = itHan.next();
        han.walk(iStream.readDouble(), iStream.readDouble());
        han.setHealthPoint(iStream.readInt());
        iStream.skipBytes(Integer.BYTES);
        if (han.getHealthPoint() == 0) {
          borderPane.getChildren().remove(han);
          itHan.remove();
          continue;
        }
        skipLenght += 2 * (Double.BYTES + Integer.BYTES);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (int i = myhans.size(); i < hansCount; i++) {
      Han han = new Han(WIDTH, HEIGHT, iStream);
      myhans.addLast(han);
      borderPane.getChildren().add(han);
      cash -= HANPRICE;
      skipLenght += 2 * (Double.BYTES + Integer.BYTES);
    }
  }

  public void updateEggs(DataInputStream iStream, int eggsCount) {
    Iterator<Egg> itEgg = eggs.iterator();
    try {
      while (itEgg.hasNext()) {
        Egg egg = itEgg.next();
        iStream.mark(2 * Double.BYTES);
        double translateX = iStream.readDouble();
        double translateY = iStream.readDouble();
        if (egg.getTranslateX() != translateX
            && egg.getTranslateY() != translateY) {
          egg.pickUp();
          itEgg.remove();
          cash += EGGPRICE;
          iStream.reset();
          continue;
        }
      }
      for (int i = eggs.size(); i < eggsCount; i++) {
        Egg egg = new Egg(borderPane, iStream);
        eggs.addLast(egg);
      }
      skipLenght += 2 * eggs.size() * Double.BYTES;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void updateCows(DataInputStream iStream, int cowsCount) {
    Iterator<Cow> itCow = cows.iterator();
    try {
      while (itCow.hasNext()) {
        Cow cow = itCow.next();
        cow.walk(iStream.readDouble(), iStream.readDouble());
        cow.setHealthPoint(iStream.readInt());
        iStream.skipBytes(Integer.BYTES);
        if (cow.getHealthPoint() == 0) {
          borderPane.getChildren().remove(cow);
          itCow.remove();
          continue;
        }
        skipLenght += 2 * (Double.BYTES + Integer.BYTES);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (int i = cows.size(); i < cowsCount; i++) {
      Cow cow = new Cow(WIDTH, HEIGHT, iStream);
      cows.addLast(cow);
      borderPane.getChildren().add(cow);
      cash -= COWPRICE;
      skipLenght += 2 * (Double.BYTES + Integer.BYTES);
    }
  }

  public void updateMilk(DataInputStream iStream, int milkCount) {
    Iterator<Milk> itMilk = milk.iterator();
    try {
      while (itMilk.hasNext()) {
        Milk tmilk = itMilk.next();
        iStream.mark(2 * Double.BYTES);
        double translateX = iStream.readDouble();
        double translateY = iStream.readDouble();
        if (tmilk.getTranslateX() != translateX
            && tmilk.getTranslateY() != translateY) {
          tmilk.pickUp();
          itMilk.remove();
          cash += MILKPRICE;
          iStream.reset();
          continue;
        }
      }

      for (int i = milk.size(); i < milkCount; i++) {
        Milk aMilk = new Milk(borderPane, iStream);
        milk.addLast(aMilk);
      }
      skipLenght += 2 * milk.size() * Double.BYTES;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean playReplay() {
    try (DataInputStream iStream = new DataInputStream(
        new BufferedInputStream(new FileInputStream(save)))) {
      iStream.skip(skipLenght);
      if (iStream.available() == 0) {
        iStream.close();
        return false;
      }
      int hansCount = iStream.readInt();
      int cowsCount = iStream.readInt();
      int eggsCount = iStream.readInt();
      int milkCount = iStream.readInt();
      barrel.setBarrelLevel(iStream.readInt());
      skipLenght += 5 * Integer.BYTES;
      updateHans(iStream, hansCount);
      updateCows(iStream, cowsCount);
      updateEggs(iStream, eggsCount);
      updateMilk(iStream, milkCount);
      iStream.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    money.setText("Cow: " + cows.size() + "/" + ANIMAL_COUNT + " Han: "
        + myhans.size() + "/" + ANIMAL_COUNT + " Cash: " + cash);
    return true;
  }
}
