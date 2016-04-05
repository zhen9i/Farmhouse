
import Game.Background;
import Game.Game;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * @author zhurko.e
 * 
 */

public class Menu extends Background {

  private final static String backgroundimg = "Start.jpg";
  private Label play;
  private Label exit;
  private Pane pane;
  private final int XLABEL = 350;
  private final int YLABEL = 680;
  private final int XLABELOFFSET = 570;
  private final int FONTSIZE = 70;

  public Menu(BorderPane p, int w, int h) {
    super(p, backgroundimg, w, h);
    pane = new Pane();

    play = new Label("Play");
    play.setTranslateX(XLABEL);
    play.setTranslateY(YLABEL);
    play.setFont(Font.font("Tahoma", FONTSIZE));
    play.setTextFill(Color.AQUA);
    play.setOnMouseEntered(event -> {
      play.setTextFill(Color.PLUM);
    });
    play.setOnMouseExited(event -> {
      play.setTextFill(Color.AQUA);
    });
    play.setOnMouseClicked(event -> {
      borderPane.getChildren().clear();
      new Game(borderPane, WIDTH, HEIGHT).draw();
    });

    exit = new Label("Exit");
    exit.setTranslateX(XLABEL + XLABELOFFSET);
    exit.setTranslateY(YLABEL);
    exit.setFont(Font.font("Tahoma", FONTSIZE));
    exit.setTextFill(Color.CHARTREUSE);
    exit.setOnMouseEntered(event -> {
      exit.setTextFill(Color.LIMEGREEN);
    });
    exit.setOnMouseExited(event -> {
      exit.setTextFill(Color.CHARTREUSE);
    });
    exit.setOnMouseClicked(event -> {
      System.exit(0);
    });
  }

  @Override
  public void draw() {
    super.draw();
    pane.getChildren().add(play);
    pane.getChildren().add(exit);
    borderPane.setCenter(pane);
  }
}
