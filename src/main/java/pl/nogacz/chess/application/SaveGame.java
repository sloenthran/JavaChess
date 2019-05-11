package pl.nogacz.chess.application;

import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Dawid Nogacz on 10.05.2019
 */
public class SaveGame {
    public boolean isSave() {
        File tempFile = new File("boardData.dat");
        return tempFile.exists();
    }

    public void save() {
        saveBoard();
        saveChessNotation();
    }

    private void saveBoard() {
        try {
            File file = new File("boardData.dat");
            ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
            output.writeObject(Board.getBoard());
            output.flush();
            output.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveChessNotation() {
        try {
            File file = new File("chessNotationData.dat");
            ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
            output.writeObject(ChessNotation.getMovesList());
            output.flush();
            output.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void load() {
        loadBoard();
        loadChessNotation();
    }

    private void loadBoard() {
        try {
            Object readObject = readObject(new File("chessNotationData.dat"));

            if(!(readObject instanceof List)) throw new Exception("Data is not a List");

            List<String> cacheNotation = (List<String>) readObject;

            ChessNotation.setMovesList(cacheNotation);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadChessNotation() {
        try {
            Object readObject = readObject(new File("boardData.dat"));

            if(!(readObject instanceof HashMap)) throw new Exception("Data is not a HashMap");

            HashMap<Coordinates, PawnClass> cacheMap = (HashMap<Coordinates, PawnClass>) readObject;

            Board.setBoard(cacheMap);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Object readObject(File file) {
        try {
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));

            Object readObject = input.readObject();
            input.close();

            return readObject;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public void remove() {
        File tempFile = new File("boardData.dat");
        tempFile.delete();
    }
}