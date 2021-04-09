package pl.nogacz.chess.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pl.nogacz.chess.application.menu.Statistics;
import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class ContributedSaveGame {

    public void save(){
        DirectoryChooser dirChooser = new DirectoryChooser();

        dirChooser.setTitle("Select location");

        File selectedDir = dirChooser.showDialog(new Stage());
        if(selectedDir != null) {
            saveBoard(selectedDir);
            saveChessNotation(selectedDir);
            saveComputer(selectedDir);
            saveStatistics(selectedDir);
        }
    }
    private void saveBoard(File dir){
        String filePath = dir.getAbsolutePath();
        filePath = filePath + "/board.txt";
        try {
            PrintWriter out = new PrintWriter(filePath);
            HashMap<Coordinates, PawnClass> board = Board.getBoard();
            for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
                String coordinates = entry.getKey().getX() + "," +  entry.getKey().getY() + ",";
                String man = board.get(entry.getKey()).getColor() + "," + board.get(entry.getKey()).getPawn();
                String line = coordinates + man;
                out.println(line);
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void saveChessNotation(File dir) {
        String filePath = dir.getAbsolutePath();
        filePath = filePath + "/chessNotation.txt";
        try {
            PrintWriter out = new PrintWriter(filePath);
            ArrayList<String> list = (ArrayList<String>) ChessNotation.getMovesList();
           for(int i =0; i<list.size();i++){
               out.println(list.get(i));
           }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveComputer(File dir) {
        try {
            String filePath = dir.getAbsolutePath();
            filePath = filePath + "/computer.json";
            FileWriter writer = new FileWriter(filePath);
            writer.write(Computer.getSkill());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveStatistics(File dir) {
        Gson gsonObj = new GsonBuilder().setPrettyPrinting().create();
        String stats = "Game win:" + Statistics.getGameWin() + "\n" +
                        "Game loss:" + Statistics.getGameLoss() + "\n"+
                        "Game draw:" + Statistics.getGameDraw();
        try {
            String filePath = dir.getAbsolutePath();
            filePath = filePath + "/statistics.json";
            FileWriter writer = new FileWriter(filePath);
            writer.write(stats);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
