package game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Statistic extends StackPane {
  public enum StatisticItem {
    PUT_EGG(1), PUT_MILK(2), ADD_HAN(3), ADD_COW(4);

    int value;

    private StatisticItem(int aValue) {
      value = aValue;
    }

    public int getValue() {
      return value;
    }
  }

  private VBox vBox = new VBox();
  private TableView<GameStatistic> table = new TableView<>();
  private ObservableList<GameStatistic> tableData =
      FXCollections.observableArrayList();
  private Button close = new Button("Close");
  private TableColumn<GameStatistic, String> id = new TableColumn<>("File");
  private TableColumn<GameStatistic, ?> pickItems = new TableColumn<>("Pick");
  private TableColumn<GameStatistic, ?> buyItems = new TableColumn<>("Buy");
  private TableColumn<GameStatistic, Integer> han = new TableColumn<>("Han");
  private TableColumn<GameStatistic, Integer> cow = new TableColumn<>("Cow");
  private TableColumn<GameStatistic, Integer> egg = new TableColumn<>("Egg");
  private TableColumn<GameStatistic, Integer> milk = new TableColumn<>("Milk");

  @SuppressWarnings("unchecked")
  public Statistic() {
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    id.setCellValueFactory(
        new PropertyValueFactory<GameStatistic, String>("id"));
    han.setCellValueFactory(
        new PropertyValueFactory<GameStatistic, Integer>("countHan"));
    cow.setCellValueFactory(
        new PropertyValueFactory<GameStatistic, Integer>("countCow"));
    egg.setCellValueFactory(
        new PropertyValueFactory<GameStatistic, Integer>("countEgg"));
    milk.setCellValueFactory(
        new PropertyValueFactory<GameStatistic, Integer>("countMilk"));
    buyItems.getColumns().addAll(han, cow);
    pickItems.getColumns().addAll(egg, milk);
    table.getColumns().addAll(id, buyItems, pickItems);
    close.setOnMouseClicked(event -> {
      setVisible(false);
    });

    initTable();

    vBox.getChildren().add(table);
    vBox.getChildren().add(close);
    vBox.setAlignment(Pos.CENTER);
    
    getChildren().add(vBox);
    setVisible(false);
  }

  void initTable() {
    ArrayList<String> array = FileSaver.getStatisticFile();
    ArrayList<GameStatistic> statistic = new ArrayList<>();
    for (String string : array) {
      try (DataInputStream iStream = new DataInputStream(
          new BufferedInputStream(new FileInputStream(string)))) {
        statistic.add(new GameStatistic(iStream,
            string.substring(5)));
        iStream.close();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    for (GameStatistic gameStatistic : statistic) {
      tableData.add(gameStatistic);
    }
    table.setItems(tableData);
  }

}
