package pl.nogacz.chess.application.menu;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * @author Dawid Nogacz on 12.05.2019
 */
public class AuthorInfo {

    public AuthorInfo() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("JavaChess");
        alert.setContentText("Dawid \"Sloenthran\" Nogacz\n" +
                "dawid@nogacz.pl\n\n" +
                "SourceCode: https://github.com/sloenthran/JavaChess");

        ButtonType exitButton = new ButtonType("OK");

        alert.getButtonTypes().setAll(exitButton);

        alert.show();
    }
}
