package chess;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class Main extends Application {

    int wW = 900;
    int wH = 900;

    @Override
    public void start (Stage primstage) {
        VBox layout = new VBox();
        GridPane root = new GridPane();
        Board b = new Board();
        b.addPieces();
        b.generateBoard(root);
        root.setOnMouseClicked(event -> b.generateBoard(root));
        Text turn = new Text("Black's Turn");
        layout.getChildren().add(root);
        root.setAlignment(Pos.CENTER);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, Color.BLACK);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setWidth(wW);
        stage.setHeight(wH);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
