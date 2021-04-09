package pl.nogacz.chess.application.menu;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Dawid Nogacz on 12.05.2019
 */
public class Statistics implements Serializable {
    private static int gameWin = 0;
    private static int gameLoss = 0;
    private static int gameDraw = 0;

    public Statistics() {
    }

    public void printInfo() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("JavaChess");
        alert.setContentText("Win: " + gameWin + "\n" +
                "Loss: " + gameLoss + "\n" +
                "Draw: " + gameDraw);

        ButtonType reset = new ButtonType("Reset");
        ButtonType exit = new ButtonType("OK");

        alert.getButtonTypes().setAll(reset, exit);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == reset){
            gameWin = 0;
            gameLoss = 0;
            gameDraw = 0;
            printInfo();
        }
    }

    public static int getGameWin() {
        return gameWin;
    }

    public static void setGameWin(int gameWin) {
        Statistics.gameWin = gameWin;
    }

    public static int getGameLoss() {
        return gameLoss;
    }

    public static void setGameLoss(int gameLoss) {
        Statistics.gameLoss = gameLoss;
    }

    public static int getGameDraw() {
        return gameDraw;
    }

    public static void setGameDraw(int gameDraw) {
        Statistics.gameDraw = gameDraw;
    }

    public void addGameWin() {
        gameWin++;
    }

    public void addGameLoss() {
        gameLoss++;
    }

    public void addGameDraw() {
        gameDraw++;
    }
}
