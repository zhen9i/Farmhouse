package Animal;

import java.util.LinkedList;

import Animal.AnimationConstant.Direction;
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

  private final static String sprite = "Han.png";
  private boolean eggready;
  private final int DELAYEGG = 15000;
  private Timeline timeline;

  public Han(int x, int y) {
    super(sprite, x, y);
    timeline = new Timeline(
        new KeyFrame(Duration.millis(DELAYEGG), ae -> setEggready(true)));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
    super.randColor();
    animation.play();
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
