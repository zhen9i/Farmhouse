package Animal;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * @author zhurko.e
 *
 */

public class Milk extends Pane {
  private final String EGGIMG = "Milk.jpg";
  private Image IMAGE;
  private ImageView imageview;
  private BorderPane borderPane;
  private boolean pickedup = false;

  Milk(BorderPane p, double x, double y) {
    borderPane = p;
    IMAGE = new Image(getClass().getResourceAsStream(EGGIMG));
    imageview = new ImageView(IMAGE);
    imageview.setTranslateX(x + IMAGE.getHeight());
    imageview.setTranslateY(y + IMAGE.getWidth());
    setTranslateX(x);
    setTranslateY(y);
    imageview.setOnMouseClicked(event -> {
      borderPane.getChildren().removeAll(imageview);
      pickedup = true;
    });
    borderPane.getChildren().add(imageview);
  }

  public boolean isPickedUp() {
    return pickedup;
  }

  public void pickUp() {
    borderPane.getChildren().removeAll(imageview);
    pickedup = true;
  }
}
