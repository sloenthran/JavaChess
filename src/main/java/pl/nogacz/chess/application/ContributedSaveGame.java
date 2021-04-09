package pl.nogacz.chess.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pl.nogacz.chess.board.Board;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class ContributedSaveGame {

    public void save(){
        DirectoryChooser dirChooser = new DirectoryChooser();

        dirChooser.setTitle("Select location");

        File selectedDir = dirChooser.showDialog(new Stage());
        if(selectedDir != null) {
            saveBoard(selectedDir);
            saveChessNotation(selectedDir);
        }
    }
    private void saveBoard(File dir){
        Gson gsonObj = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
        String json = gsonObj.toJson(Board.getBoard());
        try {
            String filePath = dir.getAbsolutePath();
            filePath = filePath + "/board.json";
            FileWriter writer = new FileWriter(filePath);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveChessNotation(File dir) {
        Gson gsonObj = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
        String json = gsonObj.toJson(ChessNotation.getMovesList());
        try {
            String filePath = dir.getAbsolutePath();
            filePath = filePath + "/chessNotation.json";
            FileWriter writer = new FileWriter(filePath);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveComputer(File dir) {
        Gson gsonObj = new GsonBuilder().setPrettyPrinting().create();
        String json = gsonObj.toJson(Computer.getSkill());
        try {
            String filePath = dir.getAbsolutePath();
            filePath = filePath + "/computer.json";
            FileWriter writer = new FileWriter(filePath);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
