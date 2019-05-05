package pl.nogacz.chess.application;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class Design {
    private static GridPane grid = new GridPane();
    private static Image lightMove = new Image("file:resources/light.png");

    public Design() {
        createBackground();
        generateEmptyBoard();
    }

    public GridPane getGrid() {
        return grid;
    }

    private void createBackground() {
        Image background = new Image("file:resources/board.png");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        grid.setBackground(new Background(backgroundImage));
    }

    private void generateEmptyBoard() {
        for (int i = 0; i < 8; i++) {
            ColumnConstraints column = new ColumnConstraints(84);
            column.setHgrow(Priority.ALWAYS);
            column.setHalignment(HPos.CENTER);
            grid.getColumnConstraints().add(column);

            RowConstraints row = new RowConstraints(85);
            row.setVgrow(Priority.ALWAYS);
            row.setValignment(VPos.CENTER);
            grid.getRowConstraints().add(row);
        }

        grid.setPadding(new Insets(39));
    }

    public static void addPawn(Coordinates coordinates, PawnClass pawn) {
        grid.add(pawn.getImage(), coordinates.getX(), coordinates.getY());
    }

    public static void addLightPawn(Coordinates coordinates, PawnClass pawn) {
        grid.add(pawn.getLightImage(), coordinates.getX(), coordinates.getY());
    }

    public static void addCheckedPawn(Coordinates coordinates, PawnClass pawn) {
        grid.add(pawn.getCheckedImage(), coordinates.getX(), coordinates.getY());
    }

    public static void addLightMove(Coordinates coordinates) {
        grid.add(new ImageView(lightMove), coordinates.getX(), coordinates.getY());
    }

    public static void removePawn(Coordinates coordinates) {
        grid.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == coordinates.getX() && GridPane.getRowIndex(node) == coordinates.getY());
    }
}