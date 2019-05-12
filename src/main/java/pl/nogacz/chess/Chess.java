package pl.nogacz.chess;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pl.nogacz.chess.application.Design;
import pl.nogacz.chess.application.Resources;
import pl.nogacz.chess.application.menu.AuthorInfo;
import pl.nogacz.chess.board.Board;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class Chess extends Application {
    Design design = new Design();
    Board board = new Board();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(design.getBorderPane(), 900, 790, Color.BLACK);
        design.getGridPane().setOnMouseClicked(event -> board.readMouseEvent(event));
        scene.setOnKeyReleased(event -> board.readKeyboard(event));

        primaryStage.setTitle("JavaChess");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
