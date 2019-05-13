package pl.nogacz.chess.application;

import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.Pawn;
import pl.nogacz.chess.pawns.PawnClass;
import pl.nogacz.chess.pawns.PawnColor;
import pl.nogacz.chess.pawns.moves.PawnMoves;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
//TODO Add hard skill
public class Computer {
    private HashMap<Coordinates, PawnClass> cacheBoard;
    private Random random = new Random();
    private int skill = 0; // 0 - Normal || 1 - Easy || 2 - Hard

    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possibleMoves = new HashSet<>();

    public Computer() {
        if(isExists()) {
            load();
        } else {
            save();
        }
    }

    private boolean isExists() {
        File tempFile = new File("gameCache/computer.dat");
        return tempFile.exists();
    }

    private void save() {
        try {
            File file = new File("gameCache/computer.dat");
            ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
            output.writeObject(skill);
            output.flush();
            output.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void load() {
        try {
            File file = new File("gameCache/computer.dat");
            ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));

            Object readObject = input.readObject();
            input.close();

            skill = (int) readObject;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void remove() {
        File tempFile = new File("gameCache/computer.dat");
        tempFile.delete();
    }

    public void setSkill(int skill) {
        this.skill = skill;

        remove();
        save();
    }

    public void getGameData() {
        cacheBoard = new HashMap<>(Board.getBoard());

        possibleMoves.clear();
        possibleKick.clear();

        for(Map.Entry<Coordinates, PawnClass> entry : cacheBoard.entrySet()) {
            if(entry.getValue().getColor().isBlack()) {
                PawnMoves moves = new PawnMoves(entry.getValue(), entry.getKey());
                if(moves.getPossibleMoves().size() > 0) {
                    possibleMoves.add(entry.getKey());
                }

                if(moves.getPossibleKick().size() > 0) {
                    possibleKick.add(entry.getKey());
                }
            }
        }
    }

    public Coordinates choosePawn() {
        switch(skill) {
            case 1: return choosePawnEasy();
            default: return choosePawnNormal();
        }
    }

    private Coordinates choosePawnEasy() {
        Object[] object = null;

        if(possibleMoves.size() > 0) {
            object = possibleMoves.toArray();
        } else if(possibleKick.size() > 0) {
            object = possibleKick.toArray();

        }

        return (Coordinates) object[random.nextInt(object.length)];
    }

    private Coordinates choosePawnNormal() {
        Object[] object = null;

        if(possibleKick.size() > 0) {
            object = possibleKick.toArray();
        } else if(possibleMoves.size() > 0) {
            object = possibleMoves.toArray();

        }

        return (Coordinates) object[random.nextInt(object.length)];
    }

    public Coordinates chooseMove(Coordinates coordinates) {
        switch(skill) {
            case 1: return chooseMoveEasy(coordinates);
            default: return chooseMoveNormal(coordinates);
        }
    }

    private Coordinates chooseMoveEasy(Coordinates coordinates) {
        PawnClass pawn = Board.getPawn(coordinates);
        PawnMoves moves = new PawnMoves(pawn, coordinates);

        if(moves.getPossibleMoves().size() > 0){
            Object[] object = moves.getPossibleMoves().toArray();
            return (Coordinates) object[random.nextInt(object.length)];
        } else if(moves.getPossibleKick().size() > 0) {
            Object[] object = moves.getPossibleKick().toArray();
            return (Coordinates) object[random.nextInt(object.length)];
        }

        return null;
    }

    private Coordinates chooseMoveNormal(Coordinates coordinates) {
        PawnClass pawn = Board.getPawn(coordinates);
        PawnMoves moves = new PawnMoves(pawn, coordinates);

        if(moves.getPossibleKick().size() > 0) {
            Object[] object = moves.getPossibleKick().toArray();
            return (Coordinates) object[random.nextInt(object.length)];
        } else if(moves.getPossibleMoves().size() > 0){
            Object[] object = moves.getPossibleMoves().toArray();
            return (Coordinates) object[random.nextInt(object.length)];
        }

        return null;
    }

    public Coordinates selectRandom(Set<Coordinates> list) {
        Object[] object = list.toArray();
        return (Coordinates) object[random.nextInt(object.length)];
    }
}