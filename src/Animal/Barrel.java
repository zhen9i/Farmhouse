package animal;

import java.util.Timer;
import java.util.TimerTask;

import animal.AnimationConstant.WaterLevel;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Barrel extends Pane {
  private final String BARREL = "/resources/Barrel.png";
  private final Image IMAGE;
  private int SCREENWIDTH;
  private int SCREENHEIGHT;
  private ImageView imageview;
  private WaterLevel waterLevel = WaterLevel.FULL;
  private Timer timer;
  private int DELAY = 20000;

  public Barrel(int screenWidth, int screenHeight) {
    SCREENHEIGHT = screenHeight;
    SCREENWIDTH = screenWidth;
    IMAGE = new Image(getClass().getResourceAsStream(BARREL));
    imageview = new ImageView(IMAGE);
    imageview.setFitHeight(AnimationConstant.HEIGHT_32);
    imageview.setFitWidth(AnimationConstant.WIDTH_32);
    imageview.setViewport(new Rectangle2D(0, 0, AnimationConstant.WIDTH_32,
        AnimationConstant.HEIGHT_32));
    getChildren().add(imageview);
    setTranslateX(SCREENWIDTH / 2);
    setTranslateY(SCREENHEIGHT / 2);
    timer = new Timer();
    timer.schedule(new TimerTask() {

      @Override
      public void run() {
        if (waterLevel != WaterLevel.EMPTY) {
          waterLevel = WaterLevel.values()[waterLevel.getWaterLevel() + 1];
        }
        redraw();
      }
    }, DELAY, DELAY);
    setOnMouseClicked(event -> {
      waterLevel = WaterLevel.FULL;
      redraw();
    });
    redraw();
  }

  private void redraw() {
    imageview.setViewport(new Rectangle2D(
        waterLevel.getWaterLevel() * AnimationConstant.WIDTH_32, 0,
        AnimationConstant.WIDTH_32, AnimationConstant.HEIGHT_32));
  }

  public int getBarrelLevel() {
    return waterLevel.getWaterLevel();
  }
  
  public void setBarrelLevel(int level) {
    waterLevel = WaterLevel.values()[level];
    redraw();
  }
  
  public boolean isEmpty() {
    return (waterLevel.getWaterLevel() == WaterLevel.EMPTY.getWaterLevel())
        ? true : false;
  }
}
