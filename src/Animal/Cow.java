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

public class Cow extends Animal {

  private final static String sprite = "Cow.png";
  private final int MILKDELAY = 60000;
  private final int COLORCOUNT = 3;

  private boolean milkready;
  Timeline timeline;

  public Cow(int x, int y) {
    super(sprite, x, y);
    timeline = new Timeline(
        new KeyFrame(Duration.millis(MILKDELAY), ae -> setMilkReady(true)));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
    randColor();
  }

  public void randColor() {
    int color = rand.nextInt(COLORCOUNT);
    offsetX = AnimationConstant.WIDTH_48 * AnimationConstant.COUNT * color;
    animation.setOffsetX(offsetX);
    animation.setOffsetY(offsetY);
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
