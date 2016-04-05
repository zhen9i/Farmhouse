package Game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * @author zhurko.e
 *
 */

public class Background {
  protected final Image IMAGE;
  protected final ImageView imgview;
  protected final int WIDTH;
  protected final int HEIGHT;
  private final int OFFSET = 10;
  protected BorderPane borderPane;


  public Background(BorderPane p, String s, int w, int h) {
    IMAGE = new Image(getClass().getResource(s).toString());
    imgview = new ImageView(IMAGE);
    WIDTH = w;
    HEIGHT = h;
    borderPane = p;

    borderPane.setPrefSize(WIDTH, HEIGHT);

    imgview.setFitWidth(WIDTH + OFFSET);
    imgview.setFitHeight(HEIGHT + OFFSET);
  }

  public void draw() {
    borderPane.getChildren().add(imgview);
  }
}
