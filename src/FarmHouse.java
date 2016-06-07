import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author zhurko.e
 *
 */

public class FarmHouse extends Application {

  private final int WIDTH = 1280;
  private final int HEIGHT = 800;
  private BorderPane root = new BorderPane();

  @Override
  public void start(Stage primaryStage) throws Exception {
    new Menu(root, WIDTH, HEIGHT).draw();
    Scene scene = new Scene(root, WIDTH, HEIGHT);
    primaryStage.setOnCloseRequest(event -> {
      try {
        stop();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    primaryStage.setResizable(false);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void stop() {
    System.exit(0);
  }
}
