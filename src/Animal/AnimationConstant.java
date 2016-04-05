package Animal;

/**
 * @author zhurko.e
 *
 */

public interface AnimationConstant {
  public final int COLUMN = 3;
  public final int COUNT = 3;
  public final int WIDTH_48 = 48;
  public final int HEIGHT_48 = 48;
  public final int BORDER = 100;
  public final int DELAY = 500;
  public final int RANDTIME = 10;
  public final int RANDCOLOR = 8;

  public enum Direction {
    DOWN(0), LEFT(1), RIGHT(2), UP(3), STOP(4);

    int value;

    Direction(int v) {
      value = v;
    }

    public int getValue() {
      return value;
    }
  }
}
