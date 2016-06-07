package game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import animal.Barrel;
import animal.Cow;
import animal.Egg;
import animal.Han;
import animal.Milk;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Replay extends Background {
  private final static String backgroundimg = "/resources/Field.jpg";
  private static String saveFile = FileSaver.getLastSaveFile();
  
  private LinkedList<Han> myhans;
  private LinkedList<Egg> eggs;
  private LinkedList<Cow> cows;
  private LinkedList<Milk> milk;
  private Barrel barrel;
  private int cash = 100000;
  private int skipLenght = 0;

  private boolean gamePlay = true;

  private AudioClip sound =
      new AudioClip(getClass().getResource("/resources/hanmusic.mp3").toString());

  private Label money;
  private HBox cashbox;

  public Replay(BorderPane pane, int screenW, int screenH, boolean java, boolean first) {
    this(pane,screenW, screenH);
    saveFile  = FileSaver.getSaveWithSort(java, first);
  }
  
  public Replay(BorderPane pane, int screenW, int screenH) {
    super(pane, backgroundimg, screenW, screenH);
    myhans = new LinkedList<Han>();
    eggs = new LinkedList<Egg>();
    cows = new LinkedList<Cow>();
    milk = new LinkedList<Milk>();
    barrel = new Barrel(screenW, screenH);

    sound.play();

    money = new Label();
    money.setFont(Font.font("Tahoma", 20));
    money.setTextFill(Color.GOLD);

    cashbox = new HBox();
    cashbox.setAlignment(Pos.CENTER_RIGHT);
    cashbox.getChildren().add(money);

    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        if (!playReplay()) {
          sound.stop();
          super.stop();
          gamePlay = false;
        }
        if (!sound.isPlaying() && playReplay()) {
          sound.play();
        }
      }
    };
    timer.start();
    draw();
  }

  public void draw() {
    super.draw();
    borderPane.setTop(cashbox);
    borderPane.getChildren().add(barrel);
  }

  public boolean getGamePlay() {
    return gamePlay;
  }

  public void updateHans(DataInputStream iStream, int hansCount) {
    Iterator<Han> itHan = myhans.iterator();
    try {
      while (itHan.hasNext()) {
        Han han = itHan.next();
        if (hansCount == 0 || han.getHealthPoint() == 0) {
          borderPane.getChildren().remove(han);
          itHan.remove();
          continue;
        }
        han.walk(iStream.readDouble(), iStream.readDouble());
        han.setHealthPoint(iStream.readInt());
        iStream.skipBytes(Integer.BYTES);
        skipLenght += 2 * (Double.BYTES + Integer.BYTES);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (int i = myhans.size(); i < hansCount; i++) {
      Han han = new Han(WIDTH, HEIGHT, iStream);
      myhans.addLast(han);
      borderPane.getChildren().add(han);
      cash -= ItemConstant.HANPRICE;
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
          cash += ItemConstant.EGGPRICE;
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
        if (cowsCount == 0 || cow.getHealthPoint() == 0) {
          borderPane.getChildren().remove(cow);
          itCow.remove();
          continue;
        }
        cow.walk(iStream.readDouble(), iStream.readDouble());
        cow.setHealthPoint(iStream.readInt());
        iStream.skipBytes(Integer.BYTES);
        skipLenght += 2 * (Double.BYTES + Integer.BYTES);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (int i = cows.size(); i < cowsCount; i++) {
      Cow cow = new Cow(WIDTH, HEIGHT, iStream);
      cows.addLast(cow);
      borderPane.getChildren().add(cow);
      cash -= ItemConstant.COWPRICE;
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
          cash += ItemConstant.MILKPRICE;
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
        new BufferedInputStream(new FileInputStream(saveFile)))) {
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
    money.setText("Cow: " + cows.size() + "/" + ItemConstant.ANIMAL_COUNT
        + " Han: " + myhans.size() + "/" + ItemConstant.ANIMAL_COUNT
        + " Cash: " + cash);
    return true;
  }
}
