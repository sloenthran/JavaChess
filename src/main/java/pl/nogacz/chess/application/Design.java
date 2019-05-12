package pl.nogacz.chess.application;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import pl.nogacz.chess.application.menu.AuthorInfo;
import pl.nogacz.chess.application.menu.EndGame;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class Design {
    private static BorderPane borderPane = new BorderPane();
    private static GridPane gridPane = new GridPane();
    private VBox vBox = new VBox();
    private static TextArea textArea = new TextArea();
    private HBox hBox = new HBox();
    private static Image lightMove = new Image(Resources.getPath("light.png"));

    public Design() {
        createBoardBackground();
        generateEmptyBoard();
        createFieldForChessNotation();
        createTopMenu();

        borderPane.setCenter(gridPane);
        borderPane.setRight(vBox);
        borderPane.setTop(hBox);
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    private void createBoardBackground() {
        Image background = new Image(Resources.getPath("board.png"));
        BackgroundSize backgroundSize = new BackgroundSize(750, 750, false, false, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        gridPane.setBackground(new Background(backgroundImage));

        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-image: url('background.png');");
    }

    private void generateEmptyBoard() {
        gridPane.setMinSize(750, 750);
        gridPane.setMaxSize(750, 750);

        for (int i = 0; i < 8; i++) {
            ColumnConstraints column = new ColumnConstraints(84);
            column.setHgrow(Priority.ALWAYS);
            column.setHalignment(HPos.CENTER);
            gridPane.getColumnConstraints().add(column);

            RowConstraints row = new RowConstraints(84);
            row.setVgrow(Priority.ALWAYS);
            row.setValignment(VPos.CENTER);
            gridPane.getRowConstraints().add(row);
        }

        gridPane.setPadding(new Insets(37, 0, 0, 37));
    }

    private void createFieldForChessNotation() {
        textArea.setEditable(false);
        textArea.setMinHeight(750);
        textArea.setMaxHeight(750);

        vBox.getChildren().add(textArea);
    }

    public void createTopMenu() {
        Button newGame = new Button("New game");
        newGame.setPrefSize(100, 20);
        newGame.setOnMouseClicked(event -> new EndGame("").newGame());

        Button author = new Button("Author");
        author.setPrefSize(100, 20);
        author.setOnMouseClicked(event -> new AuthorInfo());

        Button exitGame = new Button("Exit game");
        exitGame.setPrefSize(100, 20);
        exitGame.setOnMouseClicked(event -> System.exit(0));

        hBox.getChildren().addAll(newGame, author, exitGame);
    }

    public static void addPawn(Coordinates coordinates, PawnClass pawn) {
        gridPane.add(pawn.getImage(), coordinates.getX(), coordinates.getY());
    }

    public static void addLightPawn(Coordinates coordinates, PawnClass pawn) {
        gridPane.add(pawn.getLightImage(), coordinates.getX(), coordinates.getY());
    }

    public static void addCheckedPawn(Coordinates coordinates, PawnClass pawn) {
        gridPane.add(pawn.getCheckedImage(), coordinates.getX(), coordinates.getY());
    }

    public static void addLightMove(Coordinates coordinates) {
        gridPane.add(new ImageView(lightMove), coordinates.getX(), coordinates.getY());
    }

    public static void removePawn(Coordinates coordinates) {
        gridPane.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == coordinates.getX() && GridPane.getRowIndex(node) == coordinates.getY());
    }

    public static void setTextInTextArea(String text) {
        textArea.setText(text);
    }
}