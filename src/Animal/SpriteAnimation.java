package Animal;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * @author zhurko.e
 *
 */

public class SpriteAnimation extends Transition {

  private final ImageView imageView;
  private final int count;
  private final int columns;
  private int offsetX;
  private int offsetY;
  private final int width;
  private final int height;

  public SpriteAnimation(ImageView imageView, Duration duration, int count,
      int columns, int offsetX, int offsetY, int width, int height) {
    this.imageView = imageView;
    this.count = count;
    this.columns = columns;
    this.offsetX = offsetX;
    this.offsetY = offsetY;
    this.width = width;
    this.height = height;
    setCycleDuration(duration);
    setInterpolator(Interpolator.LINEAR);
    this.imageView
        .setViewport(new Rectangle2D(offsetX, offsetY, width, height));
  }

  void setOffsetX(int x) {
    this.offsetX = x;
  }

  void setOffsetY(int y) {
    this.offsetY = y;
  }

  @Override
  protected void interpolate(double frac) {
    final int index = Math.min((int) Math.floor(frac * count), count - 1);
    final int x = (index % columns) * width + offsetX;
    final int y = (index / columns) * height + offsetY;
    imageView.setViewport(new Rectangle2D(x, y, width, height));
  }
}
