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

public class Milk extends Pane {
  private final String EGGIMG = "/resources/Milk.png";
  private Image IMAGE;
  private ImageView imageview;
  private BorderPane borderPane;
  private boolean pickedup = false;

  public Milk(BorderPane pane, double translateX, double translateY) {
    borderPane = pane;
    IMAGE = new Image(getClass().getResourceAsStream(EGGIMG));
    imageview = new ImageView(IMAGE);
    imageview.setTranslateX(translateX + IMAGE.getHeight());
    imageview.setTranslateY(translateY + IMAGE.getWidth());
    setTranslateX(translateX);
    setTranslateY(translateY);
    imageview.setOnMouseClicked(event -> {
      pickUp();
    });
    borderPane.getChildren().add(imageview);
  }

  public Milk(BorderPane p, DataInputStream iStream) throws IOException {
    this(p, iStream.readDouble(), iStream.readDouble());
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
