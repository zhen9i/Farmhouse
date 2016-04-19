
import game.Background;
import game.Game;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * @author zhurko.e
 * 
 */

public class Menu extends Background {

  private final static String backgroundimg = "Start.jpg";
  private Label play;
  private Label exit;
  private Label replay;
  private Pane pane;
  private final int FONTSIZE = 70;
  private Game game;
  private Timeline timeline = new Timeline(
      new KeyFrame(Duration.seconds(1), ae -> checkGamePlay()));
  private AudioClip sound;
  
  public Menu(BorderPane p, int w, int h) {
    super(p, backgroundimg, w, h);
    pane = new Pane();

    timeline.setCycleCount(Animation.INDEFINITE);
    sound = new AudioClip(getClass().getResource("menu.mp3").toString());
    sound.play();
    
    replay = new Label("Replay");
    replay.setTranslateX(970);
    replay.setTranslateY(340);
    replay.setFont(Font.font("Tahoma", FONTSIZE / 2));
    replay.setTextFill(Color.SKYBLUE);
    replay.setOnMouseEntered(event -> {
      replay.setTextFill(Color.DEEPSKYBLUE);
    });
    replay.setOnMouseExited(event -> {
      replay.setTextFill(Color.SKYBLUE);
    });
    replay.setOnMouseClicked(event -> {
      sound.stop();
      borderPane.getChildren().clear();
      game = new Game(borderPane, WIDTH, HEIGHT, true);
      timeline.play();
    });

    play = new Label("Play");
    play.setTranslateX(350);
    play.setTranslateY(680);
    play.setFont(Font.font("Tahoma", FONTSIZE));
    play.setTextFill(Color.AQUA);
    play.setOnMouseEntered(event -> {
      play.setTextFill(Color.PLUM);
    });
    play.setOnMouseExited(event -> {
      play.setTextFill(Color.AQUA);
    });
    play.setOnMouseClicked(event -> {
      sound.stop();
      game = new Game(borderPane, WIDTH, HEIGHT, false);
    });

    exit = new Label("Exit");
    exit.setTranslateX(920);
    exit.setTranslateY(680);
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
    
    pane.getChildren().add(play);
    pane.getChildren().add(exit);
    pane.getChildren().add(replay);
  }
  
  @Override
  public void draw() {
    super.draw();
    borderPane.setCenter(pane);
  }
  
  private void checkGamePlay() {
    if(!game.getGamePlay()) {
      borderPane.setLeft(null);
      borderPane.setRight(null);
      borderPane.setBottom(null);
      borderPane.setTop(null);
      borderPane.setCenter(null);
      draw();
      sound.play();
      timeline.stop();
    }
  }
}
