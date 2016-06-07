
import game.Background;
import game.Game;
import game.Replay;
import game.Statistic;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * @author zhurko.e
 * 
 */

public class Menu extends Background {

  private final static String settingsimg = "/resources/Settings.png";
  private final static String backgroundimg = "/resources/Start.jpg";
  private Label play;
  private Label exit;
  private Label replay;
  private Label settings;
  private Pane pane;
  private final int FONTSIZE = 70;
  private Game game = null;
  private Replay replayGame = null;
  private Statistic statistic;
  private Timeline timeline =
      new Timeline(new KeyFrame(Duration.seconds(1), ae -> checkGamePlay()));
  private AudioClip sound;

  private MenuBox menuBox;
  private SubMenu menu, subMenu;

  private boolean javaSort = true;

  public Menu(BorderPane p, int w, int h) {
    super(p, backgroundimg, w, h);
    pane = new Pane();

    statistic = new Statistic();

    timeline.setCycleCount(Animation.INDEFINITE);
    sound = new AudioClip(getClass().getResource("/resources/menu.mp3").toString());
    sound.play();

    initReplay();
    initPlay();
    initExit();
    initSettings();

    pane.getChildren().add(play);
    pane.getChildren().add(exit);
    pane.getChildren().add(replay);
    pane.getChildren().add(settings);
    pane.getChildren().add(statistic);
  }

  private void initReplay() {
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
      replayGame = new Replay(borderPane, WIDTH, HEIGHT);
      timeline.play();
    });
  }

  private void initPlay() {
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
      borderPane.getChildren().clear();
      game = new Game(borderPane, WIDTH, HEIGHT);
    });
  }

  private void initExit() {
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
  }

  public void initSettings() {
    settings = new Label();
    settings.setTranslateX(1200);
    settings.setTranslateY(0);
    settings.setPrefSize(96, 96);
    settings
        .setBackground(new javafx.scene.layout.Background(new BackgroundImage(
            new Image(getClass().getResourceAsStream(settingsimg)),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
    settings.setOnMouseClicked(event -> {
      this.settingsMenu();
    });
  }

  private void settingsMenu() {
    MenuItem statisticItem = new MenuItem("Statistic");
    MenuItem javaSort = new MenuItem("Java sort");
    MenuItem scalaSort = new MenuItem("Scala sort");
    MenuItem firstItem = new MenuItem("Best");
    MenuItem lastItem = new MenuItem("Worst");
    MenuItem back = new MenuItem("Back");
    MenuItem closeSettings = new MenuItem("Close");
    menu = new SubMenu(statisticItem, javaSort, scalaSort, closeSettings);
    subMenu = new SubMenu(firstItem, lastItem, back);
    menuBox = new MenuBox(menu);
    menuBox.setVisible(true);
    statisticItem.setOnMouseClicked(event -> {
      this.statistic.setVisible(true);
    });
    closeSettings.setOnMouseClicked(event -> {
      this.menuBox.setVisible(false);
    });
    javaSort.setOnMouseClicked(event -> {
      this.menuBox.setSubMenu(this.subMenu);
      this.javaSort = true;
    });
    scalaSort.setOnMouseClicked(event -> {
      this.menuBox.setSubMenu(this.subMenu);
      this.javaSort = false;
    });
    back.setOnMouseClicked(event -> {
      this.menuBox.setSubMenu(this.menu);
    });
    firstItem.setOnMouseClicked(event -> {
      this.menuBox.setVisible(false);
      sound.stop();
      borderPane.getChildren().clear();
      replayGame = new Replay(borderPane, WIDTH, HEIGHT, this.javaSort, true);
      timeline.play();
    });
    lastItem.setOnMouseClicked(event -> {
      this.menuBox.setVisible(false);
      sound.stop();
      borderPane.getChildren().clear();
      replayGame =
          new Replay(borderPane, WIDTH, HEIGHT, this.javaSort, false);
      timeline.play();
    });
    borderPane.getChildren().add(menuBox);
  }

  @Override
  public void draw() {
    super.draw();
    borderPane.setCenter(pane);
  }

  private void checkGamePlay() {
    if (game != null && !game.getGamePlay()) {
      borderPane.setLeft(null);
      borderPane.setRight(null);
      borderPane.setBottom(null);
      borderPane.setTop(null);
      borderPane.setCenter(null);
      draw();
      sound.play();
      timeline.stop();
    }
    if (replayGame != null && !replayGame.getGamePlay()) {
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

  private static class MenuItem extends StackPane {
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 40;
    private static final int FONT_SIZE = 18;
    private static final double BUTTON_OPACITY = 1;
    private static final double BUTTON_ANIM_DURACTION = 1;

    private static final String FONT = "Arial";

    private Rectangle bd;
    private Text text;
    private FillTransition st;

    {
      bd = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT, Color.WHITE);
      bd.setOpacity(BUTTON_OPACITY);
      bd.setFill(Color.LIGHTBLUE);
      st = new FillTransition(Duration.seconds(BUTTON_ANIM_DURACTION), bd);
    }

    {
      this.setOnMouseEntered(event -> {
        st.setFromValue(Color.LIGHTBLUE);
        st.setToValue(Color.SPRINGGREEN);
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
      });
      this.setOnMouseExited(event -> {
        st.stop();
        bd.setFill(Color.LIGHTBLUE);
      });
    }

    public MenuItem(String name) {
      text = new Text(name);
      text.setFill(Color.WHITE);
      text.setFont(Font.font(FONT, FontWeight.BOLD, FONT_SIZE));

      setAlignment(Pos.CENTER);
      getChildren().addAll(bd, text);
    }
  }

  private static class MenuBox extends Pane {
    private static SubMenu submenu;

    public MenuBox(SubMenu submenu) {
      MenuBox.submenu = submenu;

      setVisible(false);
      getChildren().addAll(submenu);
    }

    public void setSubMenu(SubMenu submenu) {
      getChildren().remove(MenuBox.submenu);
      MenuBox.submenu = submenu;
      getChildren().add(MenuBox.submenu);
    }
  }

  private static class SubMenu extends VBox {
    public SubMenu(MenuItem... items) {
      setTranslateX(1190);
      setTranslateY(0);
      for (MenuItem item : items) {
        getChildren().addAll(item);
      }
    }
  }
}
