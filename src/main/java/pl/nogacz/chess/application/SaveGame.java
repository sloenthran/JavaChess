package pl.nogacz.chess.application;

import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;

import java.io.*;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Dawid Nogacz on 10.05.2019
 */
public class SaveGame {
    public boolean isSave() {
        File tempFile = new File("gameData.dat");
        return tempFile.exists();
    }

    public void save() {
        try {
            File file = new File("gameData.dat");
            ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
            output.writeObject(Board.getBoard());
            output.flush();
            output.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void load() {
        try {
            File file = new File("gameData.dat");
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));

            Object readObject = input.readObject();
            input.close();

            if(!(readObject instanceof HashMap)) throw new Exception("Data is not a HashMap");

            HashMap<Coordinates, PawnClass> cacheMap = (HashMap<Coordinates, PawnClass>) readObject;

            Board.setBoard(cacheMap);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void remove() {
        File tempFile = new File("gameData.dat");
        tempFile.delete();
    }
}