package pl.nogacz.chess.application;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * @author Dawid Nogacz on 07.05.2019
 */
public class EndGame {

    public EndGame(String message) {
        printDialog(message);
    }

    private void printDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sloenthran :: Chess");
        alert.setHeaderText(message);
        alert.setContentText("Choose your option.");

        ButtonType newGameButton = new ButtonType("New game");
        ButtonType exitButton = new ButtonType("Exit");

        alert.getButtonTypes().setAll(newGameButton, exitButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == newGameButton){
            newGame();
        } else {
            System.exit(0);
        }
    }

    private void newGame() {
        //TODO newGame
        System.out.println("New game");
    }
}
