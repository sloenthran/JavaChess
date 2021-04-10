package pl.nogacz.chess.application;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pl.nogacz.chess.application.menu.Statistics;
import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;


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
        String buffer = "";
        try {
            PrintWriter out = new PrintWriter(filePath);
            HashMap<Coordinates, PawnClass> board = Board.getBoard();
            for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
                String coordinates = entry.getKey().getX() + "," +  entry.getKey().getY() + ",";
                String man = board.get(entry.getKey()).getColor() + "," + board.get(entry.getKey()).getPawn();
                String line = coordinates + man;
                buffer += line;
                out.println(line);
            }
            long crc = computeCRC(buffer.getBytes());
            out.println(crc);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void saveChessNotation(File dir) {
        String filePath = dir.getAbsolutePath();
        filePath = filePath + "/chessNotation.txt";
        String buffer = "";
        try {
            PrintWriter out = new PrintWriter(filePath);
            ArrayList<String> list = (ArrayList<String>) ChessNotation.getMovesList();
           for(int i =0; i<list.size();i++){
               buffer += list.get(i);
               out.println(list.get(i));
           }

           long crc = computeCRC(buffer.getBytes());
           out.println(crc);
           out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveComputer(File dir) {
        String buffer = "";
        try {
            String filePath = dir.getAbsolutePath();
            filePath = filePath + "/computer.txt";
            PrintWriter writer = new PrintWriter(filePath);
            writer.println(Computer.getSkill());
            buffer = String.valueOf(Computer.getSkill());
            long crc = computeCRC(buffer.getBytes());
            writer.println(crc);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveStatistics(File dir) {
        String stats = "Game win:" + Statistics.getGameWin() + "\n" +
                        "Game loss:" + Statistics.getGameLoss() + "\n"+
                        "Game draw:" + Statistics.getGameDraw();
        try {
            String filePath = dir.getAbsolutePath();
            filePath = filePath + "/statistics.txt";
            PrintWriter writer = new PrintWriter(filePath);
            writer.println(stats);
            long crc = computeCRC(stats.getBytes());
            writer.println(crc);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long computeCRC(byte[] byteArr){
        CRC32 crcmaker = new CRC32();
        crcmaker.update(byteArr, 0, byteArr.length);
        return  crcmaker.getValue();
    }

}
