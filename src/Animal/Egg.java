package animal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * @author zhurko.e
 *
 */

public class Egg extends Pane {
  private final String EGGIMG = "/resources/Egg.png";
  private Image IMAGE;
  private ImageView imageview;
  private final BorderPane borderPane;
  private boolean pickedup = false;

  public Egg(BorderPane pane, double translateX, double translateY) {
    borderPane = pane;
    IMAGE = new Image(getClass().getResourceAsStream(EGGIMG));
    imageview = new ImageView(IMAGE);
    setTranslateX(translateX);
    setTranslateY(translateY);
    imageview.setTranslateX(translateX);
    imageview.setTranslateY(translateY);
    imageview.setOnMouseClicked(event -> {
      pickUp();
    });
    borderPane.getChildren().add(imageview);
  }

  public Egg(BorderPane pane, DataInputStream iStream) throws IOException {
    this(pane, iStream.readDouble(), iStream.readDouble());
  }

  public boolean isPickedUp() {
    return pickedup;
  }

  public void pickUp() {
    borderPane.getChildren().removeAll(imageview);
    pickedup = true;
  }
  
  public void save(DataOutputStream oStream) {
    try {
      oStream.writeDouble(getTranslateX());
      oStream.writeDouble(getTranslateY());
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }
}
