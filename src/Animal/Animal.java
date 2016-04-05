
package Animal;

import java.util.Random;

import Animal.AnimationConstant.Direction;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * @author zhurko.e
 *
 */

public class Animal extends Pane {

  protected final Image IMAGE;
  protected final int screenX;
  protected final int screenY;

  protected ImageView imageview;
  protected Random rand;
  protected int offsetX = 0;
  protected int offsetY = 0;
  protected SpriteAnimation animation;
  protected Thread directionthread;
  protected Direction direction = Direction.STOP;

  public Animal(String s, int x, int y) {
    screenX = x;
    screenY = y;

    rand = new Random();
    IMAGE = new Image(getClass().getResourceAsStream(s));
    imageview = new ImageView(IMAGE);
    imageview.setFitHeight(AnimationConstant.HEIGHT_48);
    imageview.setFitWidth(AnimationConstant.WIDTH_48);
    imageview.setViewport(new Rectangle2D(offsetX, offsetY,
        AnimationConstant.WIDTH_48, AnimationConstant.HEIGHT_48));
    getChildren().add(imageview);
    setTranslateX(screenX / 2
        + rand.nextInt(AnimationConstant.BORDER + AnimationConstant.BORDER)
        - AnimationConstant.BORDER);
    setTranslateY(screenY / 2
        + rand.nextInt(AnimationConstant.BORDER + AnimationConstant.BORDER)
        - AnimationConstant.BORDER);
    animation = new SpriteAnimation(imageview,
        Duration.millis(AnimationConstant.DELAY), AnimationConstant.COUNT,
        AnimationConstant.COLUMN, offsetX, offsetY,
        AnimationConstant.WIDTH_48, AnimationConstant.WIDTH_48);
    directionthread = new Thread() {
      public void run() {
        try {
          while (true) {
            direction = Direction.values()[rand.nextInt(5)];
            Thread.sleep(rand.nextInt(AnimationConstant.RANDTIME)
                * AnimationConstant.DELAY);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
    directionthread.start();
  }

  public void walk() {
    animation.setOffsetY(
        offsetY + AnimationConstant.HEIGHT_48 * direction.getValue());
    animation.play();
    switch (direction) {
      case DOWN:
        this.setTranslateY(this.getTranslateY() + 1);
        break;
      case LEFT:
        this.setTranslateX(this.getTranslateX() - 1);
        break;
      case RIGHT:
        this.setTranslateX(this.getTranslateX() + 1);
        break;
      case UP:
        this.setTranslateY(this.getTranslateY() - 1);
        break;
      case STOP:
        animation.pause();
        break;
    }
    checkBorder();
  }

  public void checkBorder() {
    if ((getTranslateX() - 1) < AnimationConstant.BORDER
        || ((getTranslateX() + 1) > (screenX - AnimationConstant.BORDER))) {
      direction =
          (direction == Direction.LEFT) ? Direction.RIGHT : Direction.LEFT;
    } else if ((getTranslateY() - 1) < AnimationConstant.BORDER
        || (getTranslateY() + 1) > (screenY - AnimationConstant.BORDER)) {
      direction =
          (direction == Direction.DOWN) ? Direction.UP : Direction.DOWN;
    }
  }

  public void randColor() {
    int color = rand.nextInt(AnimationConstant.RANDCOLOR);
    if (color <= AnimationConstant.COUNT) {
      offsetX = AnimationConstant.WIDTH_48 * AnimationConstant.COUNT * color;
    } else {
      offsetX = AnimationConstant.WIDTH_48 * AnimationConstant.COLUMN
          * (color - AnimationConstant.COUNT - 1);
      offsetY = AnimationConstant.HEIGHT_48 * (AnimationConstant.COUNT + 1);
    }
    animation.setOffsetX(offsetX);
    animation.setOffsetY(offsetY);
  }
}
