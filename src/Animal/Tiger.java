package Animal;

import Animal.AnimationConstant.Direction;

/**
 * @author zhurko.e
 *
 */

public class Tiger extends Animal {

  private final static String sprite = "Tiger.png";

  public Tiger(int x, int y) {
    super(sprite, x, y);
    super.randColor();
    direction = Direction.DOWN;
    animation.setOffsetY(
        offsetY + AnimationConstant.HEIGHT_48 * direction.getValue());
    animation.play();
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
