package animal;

import java.io.DataInputStream;
import java.io.IOException;

import animal.AnimationConstant.Direction;

/**
 * @author zhurko.e
 *
 */

public class Tiger extends Animal {

  private final static String sprite = "/resources/Tiger.png";

  public Tiger(int screenW, int screenH) {
    super(sprite, screenW, screenH);
    super.randColor();
    direction = Direction.DOWN;
    animation.setOffsetY(
        offsetY + AnimationConstant.HEIGHT_48 * direction.getValue());
    animation.play();
  }

  public Tiger(int screenW, int screenH, DataInputStream iStream) {
    super(sprite, screenW, screenH, iStream);
    try {
      setColor(iStream.readInt());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public boolean take(double translateX, double translateY) {
    if (this.getTranslateX() > translateX) {
      direction = Direction.LEFT;
      animation.setOffsetY(
          offsetY + AnimationConstant.HEIGHT_48 * direction.getValue());
      animation.play();
      super.setTranslateX(super.getTranslateX() - 1);
    } else if (this.getTranslateX() < translateX) {
      direction = Direction.RIGHT;
      animation.setOffsetY(
          offsetY + AnimationConstant.HEIGHT_48 * direction.getValue());
      animation.play();
      this.setTranslateX(this.getTranslateX() + 1);
    } else if (this.getTranslateY() < translateY) {
      direction = Direction.DOWN;
      animation.setOffsetY(
          offsetY + AnimationConstant.HEIGHT_48 * direction.getValue());
      animation.play();
      this.setTranslateY(this.getTranslateY() + 1);
    } else if (this.getTranslateY() > translateY) {
      direction = Direction.UP;
      animation.setOffsetY(
          offsetY + AnimationConstant.HEIGHT_48 * direction.getValue());
      animation.play();
      this.setTranslateY(this.getTranslateY() - 1);
    } else {
      return true;
    }
    return false;
  }
}
