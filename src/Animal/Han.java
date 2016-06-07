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

public class Han extends Animal {

  private final static String sprite = "/resources/Han.png";
  private boolean eggready;
  private final int DELAYEGG = 15000;
  private Timeline timeline = new Timeline(
      new KeyFrame(Duration.millis(DELAYEGG), ae -> setEggready(true)));

  public Han(int screenW, int screenH) {
    super(sprite, screenW, screenH);
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
    super.randColor();
    directionthread.start();
  }

  public Han(int screenW, int screenH, DataInputStream iStream) {
    super(sprite, screenW, screenH, iStream);
    timeline.setCycleCount(Animation.INDEFINITE);
    try {
      setColor(iStream.readInt());
    } catch (IOException e) {
      e.printStackTrace();
    }
    timeline.play();
  }

  public void putEgg(BorderPane p, LinkedList<Egg> eggs) {
    if (eggready && direction == Direction.STOP) {
      eggs.addLast(new Egg(p, super.getTranslateX(), super.getTranslateY()));
      eggready = false;
      timeline.playFromStart();
    }
  }

  private void setEggready(boolean b) {
    eggready = b;
  }
}
