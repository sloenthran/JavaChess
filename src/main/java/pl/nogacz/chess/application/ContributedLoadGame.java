package pl.nogacz.chess.application;

import com.google.gson.Gson;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ContributedLoadGame {

    public void load(){
        DirectoryChooser dirChooser = new DirectoryChooser();

        dirChooser.setTitle("Choose location");

        File selectedDir = dirChooser.showDialog(new Stage());
        if(selectedDir != null) {
            loadBoard(selectedDir);
            loadChessNotation(selectedDir);
        }
    }

    private void loadBoard(File dir){

        String filePath = dir.getAbsolutePath();
        filePath = filePath + "/board.json";
        String json = null;
        try {
            json = new Scanner(new File(filePath)).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<Coordinates, PawnClass> cacheMap = new Gson().fromJson(json, HashMap.class);
        Board.setBoard(cacheMap);

    }

    private void loadChessNotation(File dir) {
        String filePath = dir.getAbsolutePath();
        filePath = filePath + "/chessNotation.json";
        String json = null;
        try {
            json = new Scanner(new File(filePath)).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> cacheNotation= new Gson().fromJson(json, List.class);
        ChessNotation.setMovesList(cacheNotation);
    }

    private void loadComputer(File dir) {
        try {
            String filePath = dir.getAbsolutePath();
            filePath = filePath + "/computer.json";
            int skill = Integer.parseInt(new Scanner(new File(filePath)).useDelimiter("\\Z").next());
            Computer.setSkill(skill);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
