
package Game;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import Animal.Cow;
import Animal.Egg;
import Animal.Han;
import Animal.Milk;
import Animal.Tiger;
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
  private final int ICON = 64;
  private final int ANIMAL_COUNT = 10;
  private final int SPEED_MAX = 3;

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
  private final int EGGPRICE = 50;
  private final int HANPRICE = 100;
  private final int MILKPRICE = 250;
  private final int COWPRICE = 5000;
  private boolean cheating = false;
  private int speed = 1;

  private Tiger tiger;

  public Game(BorderPane p, int w, int h) {
    super(p, backgroundimg, w, h);
    myhans = new LinkedList<Han>();
    eggs = new LinkedList<Egg>();
    cows = new LinkedList<Cow>();
    milk = new LinkedList<Milk>();

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

    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        update();
        cheat();
      }
    };
    timer.start();
  }

  @Override
  public void draw() {
    super.draw();
    borderPane.setLeft(vbox);
    borderPane.setTop(cashbox);
  }

  public void update() {
    Iterator<Egg> ite = eggs.iterator();
    Iterator<Milk> itm = milk.iterator();

    while (ite.hasNext()) {
      Egg egg = ite.next();
      if (egg.isPickedUp()) {
        ite.remove();
        cash += EGGPRICE;
      }
    }
    while (itm.hasNext()) {
      Milk tmilk = itm.next();
      if (tmilk.isPickedUp()) {
        itm.remove();
        cash += MILKPRICE;
      }
    }
    for (Han han : myhans) {
      han.walk();
      han.putEgg(borderPane, eggs);
    }
    for (Cow cow : cows) {
      cow.walk();
      cow.putMilk(borderPane, milk);
    }
    money.setText("Cow: " + cows.size() + "/" + ANIMAL_COUNT + " Han: "
        + myhans.size() + "/" + ANIMAL_COUNT + " Cash: " + cash);
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
}
