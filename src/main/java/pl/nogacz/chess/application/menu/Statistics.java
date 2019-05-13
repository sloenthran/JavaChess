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
    private int gameWin = 0;
    private int gameLoss = 0;
    private int gameDraw = 0;

    public Statistics() {
        if(isExists()) {
            load();
        } else {
            save();
        }
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
            remove();

            gameWin = 0;
            gameLoss = 0;
            gameDraw = 0;

            save();

            printInfo();
        }
    }

    private boolean isExists() {
        File tempFile = new File("gameCache/statistics.dat");
        return tempFile.exists();
    }

    private void save() {
        try {
            ArrayList<Integer> stats = new ArrayList<>();
            stats.add(gameWin);
            stats.add(gameLoss);
            stats.add(gameDraw);

            File file = new File("gameCache/statistics.dat");
            ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
            output.writeObject(stats);
            output.flush();
            output.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void load() {
        try {
            File file = new File("gameCache/statistics.dat");
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));

            Object readObject = input.readObject();
            input.close();

            if(!(readObject instanceof ArrayList)) throw new Exception("Data is not a ArrayList");

            ArrayList<Integer> cacheMap = (ArrayList<Integer>) readObject;

            gameWin = cacheMap.get(0);
            gameLoss = cacheMap.get(1);
            gameDraw = cacheMap.get(2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void remove() {
        File tempFile = new File("gameCache/statistics.dat");
        tempFile.delete();
    }

    public void addGameWin() {
        gameWin++;
        remove();
        save();
    }

    public void addGameLoss() {
        gameLoss++;
        remove();
        save();
    }

    public void addGameDraw() {
        gameDraw++;
        remove();
        save();
    }
}
