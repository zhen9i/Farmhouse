package animal;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;

import animal.AnimationConstant.Direction;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

/**
 * @author zhurko.e
 *
 */

public class Cow extends Animal {

  private final static String sprite = "Cow.png";
  private final int MILKDELAY = 60000;
  private final int COLORCOUNT = 3;

  private boolean milkready;
  Timeline timeline = new Timeline(
      new KeyFrame(Duration.millis(MILKDELAY), ae -> setMilkReady(true)));

  public Cow(int screenW, int screenH) {
    super(sprite, screenW, screenH);
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
    randColor();
    directionthread.start();
  }
  
  public Cow(int screenW, int screenH, DataInputStream iStream) {
    super(sprite, screenW, screenH, iStream);
    timeline.setCycleCount(Animation.INDEFINITE);
    try {
      setColor(iStream.readInt());
    } catch (IOException e) {
      e.printStackTrace();
    }
    timeline.play();
  }

  public void randColor() {
    int color = rand.nextInt(COLORCOUNT);
    setColor(color);
  }

  public void putMilk(BorderPane p, LinkedList<Milk> milk) {
    if (milkready && direction == Direction.STOP) {
      milk.addLast(new Milk(p, super.getTranslateX(), super.getTranslateY()));
      milkready = false;
      timeline.playFromStart();
    }
  }

  void setMilkReady(boolean m) {
    milkready = m;
  }
}
