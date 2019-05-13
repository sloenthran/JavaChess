package pl.nogacz.chess.application.menu;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import pl.nogacz.chess.board.Board;

import java.util.Optional;

/**
 * @author Dawid Nogacz on 12.05.2019
 */
public class Difficulty {
    public Difficulty() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("JavaChess");
        alert.setContentText("Select difficulty");

        ButtonType easy = new ButtonType("Easy");
        ButtonType normal = new ButtonType("Normal");
        ButtonType hard = new ButtonType("Hard");

        alert.getButtonTypes().setAll(easy, normal, hard);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == easy){
            Board.setComputerSkill(1);
        } else if (result.get() == hard){
            Board.setComputerSkill(2);
        } else {
            Board.setComputerSkill(0);
        }
    }
}
