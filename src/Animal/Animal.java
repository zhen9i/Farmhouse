
package animal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import animal.AnimationConstant.Direction;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * @author zhurko.e
 * @Class Animal Class create animal, generate random color of each animal.
 */

public class Animal extends Pane {

  protected final Image IMAGE;
  protected int SCREENWIDTH;
  protected int SCREENHEIGHT;

  protected ImageView imageview;
  protected Random rand = new Random();
  protected int offsetX = 0;
  protected int offsetY = 0;
  protected SpriteAnimation animation;
  protected Thread directionthread;
  protected Direction direction = Direction.STOP;
  protected int DIRECTION_VARIANT = 5;
  protected int healthPoint = AnimationConstant.MAXHEALTHPOINT;

  /**
   * 
   * @param imageName - path to animal's image.
   * @param screenW - width of game screen
   * @param screenH - height of game screen
   */
  public Animal(String imageName, int screenW, int screenH) {
    SCREENWIDTH = screenW;
    SCREENHEIGHT = screenH;

    IMAGE = new Image(getClass().getResourceAsStream(imageName));
    imageview = new ImageView(IMAGE);
    imageview.setFitHeight(AnimationConstant.HEIGHT_48);
    imageview.setFitWidth(AnimationConstant.WIDTH_48);
    imageview.setViewport(new Rectangle2D(offsetX, offsetY,
        AnimationConstant.WIDTH_48, AnimationConstant.HEIGHT_48));
    getChildren().add(imageview);
    setTranslateX(SCREENWIDTH / 2
        + rand.nextInt(AnimationConstant.BORDER + AnimationConstant.BORDER)
        - AnimationConstant.BORDER);
    setTranslateY(SCREENHEIGHT / 2
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
            direction = Direction.values()[rand.nextInt(DIRECTION_VARIANT)];
            Thread.sleep(rand.nextInt(AnimationConstant.RANDTIME)
                * AnimationConstant.DELAY);
            if (getHealthPoint() == 0) {
              break;
            }
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
  }

  /**
   * 
   * @param imageName - path to animal's image
   * @param screenW - width of game screen
   * @param screenH - height of game screen
   * @param iStream - stream to read information about animal from binary file
   */
  public Animal(String imageName, int screenW, int screenH,
      DataInputStream iStream) {
    this(imageName, screenW, screenH);
    try {
      setTranslateX(iStream.readDouble());
      setTranslateY(iStream.readDouble());
      setHealthPoint(iStream.readInt());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @Method This method determine animals direction. According to each directions, picture
   *         directions changed
   * 
   */
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

  /**
   * 
   * @param translateX - coordinate X to move animal
   * @param translateY - coordinate Y to move animal
   */
  public void walk(double translateX, double translateY) {
    if (getTranslateX() > translateX) {
      direction = Direction.LEFT;
      animation.setOffsetY(
          offsetY + AnimationConstant.HEIGHT_48 * direction.getValue());
      animation.play();
      setTranslateX(getTranslateX() - 1);
    } else if (getTranslateX() < translateX) {
      direction = Direction.RIGHT;
      animation.setOffsetY(
          offsetY + AnimationConstant.HEIGHT_48 * direction.getValue());
      animation.play();
      setTranslateX(getTranslateX() + 1);
    } else if (getTranslateY() < translateY) {
      direction = Direction.DOWN;
      animation.setOffsetY(
          offsetY + AnimationConstant.HEIGHT_48 * direction.getValue());
      animation.play();
      setTranslateY(getTranslateY() + 1);
    } else if (getTranslateY() > translateY) {
      direction = Direction.UP;
      animation.setOffsetY(
          offsetY + AnimationConstant.HEIGHT_48 * direction.getValue());
      animation.play();
      setTranslateY(getTranslateY() - 1);
    } else {
      direction = Direction.STOP;
      animation.pause();
    }
  }

  /**
   * @Method Check that animal don't go from the game screen
   */
  public void checkBorder() {
    if ((getTranslateX() - 1) < AnimationConstant.BORDER || ((getTranslateX()
        + 1) > (SCREENWIDTH - AnimationConstant.BORDER))) {
      direction =
          (direction == Direction.LEFT) ? Direction.RIGHT : Direction.LEFT;
    } else if ((getTranslateY() - 1) < AnimationConstant.BORDER
        || (getTranslateY() + 1) > (SCREENHEIGHT
            - AnimationConstant.BORDER)) {
      direction =
          (direction == Direction.DOWN) ? Direction.UP : Direction.DOWN;
    }
  }

  /**
   * @Method Generate animal's color
   */
  public void randColor() {
    int color = rand.nextInt(AnimationConstant.RANDCOLOR);
    setColor(color);
  }

  /**
   * 
   * @param color - index number of color
   */
  public void setColor(int color) {
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

  /**
   * 
   * @param hpoint - amount of health point
   */
  public void setHealthPoint(int hpoint) {
    healthPoint = hpoint;
  }

  /**
   * 
   * @return amount of animal's health point
   */
  public int getHealthPoint() {
    return healthPoint;
  }

  /**
   * 
   * @param oStream stream to write personal information to binary file
   */
  public void save(DataOutputStream oStream) {
    try {
      oStream.writeDouble(getTranslateX());
      oStream.writeDouble(getTranslateY());
      oStream.writeInt(healthPoint);
      oStream.writeInt(
          (offsetY >= AnimationConstant.HEIGHT_48 * AnimationConstant.COUNT
              ? (AnimationConstant.COUNT + 1) : 0)
              + offsetX
                  / (AnimationConstant.WIDTH_48 * AnimationConstant.COUNT));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
