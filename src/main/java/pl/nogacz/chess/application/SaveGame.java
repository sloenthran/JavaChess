package pl.nogacz.chess.application;

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
import java.util.zip.CRC32;


public class SaveGame {

    private File selectedDir;

    public void save(){
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select location");
        this.selectedDir = dirChooser.showDialog(new Stage());
        try {
            saveToDirectory(selectedDir);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public void saveToDirectory(File dir) throws FileNotFoundException {
        this.selectedDir = dir;
        if(dir != null) {
            saveBoard();
            saveChessNotation();
            saveComputer();
            saveStatistics();
        }
    }


    private void saveBoard() throws FileNotFoundException {
        String filePath = selectedDir.getAbsolutePath();
        filePath = filePath + "/board.txt";
        StringBuilder buffer = new StringBuilder();

        PrintWriter out = new PrintWriter(filePath);
        HashMap<Coordinates, PawnClass> board = Board.getBoard();
        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            String coordinates = entry.getKey().getX() + "," +  entry.getKey().getY() + ",";
            String man = board.get(entry.getKey()).getColor() + "," + board.get(entry.getKey()).getPawn();
            String line = coordinates + man;
            buffer.append(line);
            out.println(line);
        }
        long crc = computeCRC(buffer.toString().getBytes());
        out.println(crc);
        out.close();


    }

    private void saveChessNotation() throws FileNotFoundException {
        String filePath = selectedDir.getAbsolutePath();
        filePath = filePath + "/chessNotation.txt";
        StringBuilder buffer = new StringBuilder();
        PrintWriter out = new PrintWriter(filePath);
        ArrayList<String> list = (ArrayList<String>) ChessNotation.getMovesList();
        for (String s : list) {
            buffer.append(s);
            out.println(s);
        }

       long crc = computeCRC(buffer.toString().getBytes());
       out.println(crc);
       out.close();

    }

    private void saveComputer() throws FileNotFoundException {
        String buffer;
        String filePath = selectedDir.getAbsolutePath();
        filePath = filePath + "/computer.txt";
        PrintWriter writer = new PrintWriter(filePath);
        writer.println(Computer.getSkill());
        buffer = String.valueOf(Computer.getSkill());
        long crc = computeCRC(buffer.getBytes());
        writer.println(crc);
        writer.flush();
        writer.close();

    }

    private void saveStatistics() throws FileNotFoundException {
        String stats = "Game win:" + Statistics.getGameWin() + "\n" +
                        "Game loss:" + Statistics.getGameLoss() + "\n"+
                        "Game draw:" + Statistics.getGameDraw();
        String filePath = selectedDir.getAbsolutePath();
        filePath = filePath + "/statistics.txt";
        PrintWriter writer = new PrintWriter(filePath);
        writer.println(stats);
        long crc = computeCRC(stats.getBytes());
        writer.println(crc);
        writer.flush();
        writer.close();

    }

    private long computeCRC(byte[] byteArr){
        CRC32 crcmaker = new CRC32();
        crcmaker.update(byteArr, 0, byteArr.length);
        return  crcmaker.getValue();
    }

}
