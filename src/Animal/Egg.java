package Animal;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * @author zhurko.e
 *
 */

public class Egg extends Pane {
  private final String EGGIMG = "Egg.png";
  private Image IMAGE;
  private ImageView imageview;
  private final BorderPane borderPane;
  private boolean pickedup = false;

  Egg(BorderPane p, double x, double y) {
    borderPane = p;
    IMAGE = new Image(getClass().getResourceAsStream(EGGIMG));
    imageview = new ImageView(IMAGE);
    setTranslateX(x);
    setTranslateY(y);
    imageview.setTranslateX(x);
    imageview.setTranslateY(y);
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
