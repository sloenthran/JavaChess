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
public class Computer {
    private HashMap<Coordinates, PawnClass> cacheBoard;
    private Random random = new Random();
    private int skill = 0; // 0 - Normal || 1 - Easy || 2 - Hard

    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKickAndNotIsEnemyKickMe = new HashSet<>();

    private BoardPoint boardPoint = new BoardPoint();

    public Computer() {
        if (isExists()) {
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
        possibleKickAndNotIsEnemyKickMe.clear();

        for (Map.Entry<Coordinates, PawnClass> entry : cacheBoard.entrySet()) {
            if (entry.getValue().getColor().isBlack()) {
                PawnMoves moves = new PawnMoves(entry.getValue(), entry.getKey());
                if (moves.getPossibleMoves().size() > 0) {
                    possibleMoves.add(entry.getKey());
                }

                if (moves.getPossibleKick().size() > 0) {
                    possibleKick.add(entry.getKey());
                }
            }
        }
    }

    public Coordinates choosePawn() {
        switch (skill) {
            case 1: return choosePawnEasy();
            case 2: return choosePawnHard();
            default: return choosePawnNormal();
        }
    }

    private Coordinates choosePawnEasy() {
        Object[] object = null;

        if (possibleMoves.size() > 0) {
            object = possibleMoves.toArray();
        } else if (possibleKick.size() > 0) {
            object = possibleKick.toArray();

        }

        return (Coordinates) object[random.nextInt(object.length)];
    }

    private Coordinates choosePawnNormal() {
        Object[] object = null;

        if (possibleKick.size() > 0) {
            object = possibleKick.toArray();
        } else if (possibleMoves.size() > 0) {
            object = possibleMoves.toArray();
        }

        return (Coordinates) object[random.nextInt(object.length)];
    }

    private Coordinates choosePawnHard() {
        int minNumber = -1000;

        Set<Coordinates> cachePawn = new HashSet<>();
        cachePawn.addAll(possibleKick);
        cachePawn.addAll(possibleMoves);

        Set<Coordinates> cachePossiblePawn = new HashSet<>();

        for(Coordinates coordinates : cachePawn) {

            PawnMoves moves = new PawnMoves(Board.getPawn(coordinates), coordinates);

            Set<Coordinates> cacheMoves = new HashSet<>();
            cacheMoves.addAll(moves.getPossibleKick());
            cacheMoves.addAll(moves.getPossibleMoves());

            for(Coordinates moveCoordinates : cacheMoves) {
                PawnClass oldPawn = Board.addPawnWithoutDesign(moveCoordinates, Board.getPawn(coordinates));

                int point = boardPoint.calculateBoard();

                if(point > minNumber) {
                    minNumber = point;
                }

                Board.removePawnWithoutDesign(moveCoordinates);

                if (oldPawn != null) {
                    Board.addPawnWithoutDesign(moveCoordinates, oldPawn);
                }
            }

            for(Coordinates moveCoordinates : cacheMoves) {
                PawnClass oldPawn = Board.addPawnWithoutDesign(moveCoordinates, Board.getPawn(coordinates));

                int point = boardPoint.calculateBoard();

                if(point == minNumber) {
                    cachePossiblePawn.add(coordinates);
                }

                Board.removePawnWithoutDesign(moveCoordinates);

                if (oldPawn != null) {
                    Board.addPawnWithoutDesign(moveCoordinates, oldPawn);
                }
            }
        }

        return (Coordinates) cachePossiblePawn.toArray()[random.nextInt(cachePossiblePawn.size())];
    }

    public Coordinates chooseMove(Coordinates coordinates) {
        switch (skill) {
            case 1: return chooseMoveEasy(coordinates);
            case 2: return chooseMoveHard(coordinates);
            default: return chooseMoveNormal(coordinates);
        }
    }

    private Coordinates chooseMoveEasy(Coordinates coordinates) {
        PawnClass pawn = Board.getPawn(coordinates);
        PawnMoves moves = new PawnMoves(pawn, coordinates);

        if (moves.getPossibleMoves().size() > 0) {
            Object[] object = moves.getPossibleMoves().toArray();
            return (Coordinates) object[random.nextInt(object.length)];
        } else if (moves.getPossibleKick().size() > 0) {
            Object[] object = moves.getPossibleKick().toArray();
            return (Coordinates) object[random.nextInt(object.length)];
        }

        return null;
    }

    private Coordinates chooseMoveNormal(Coordinates coordinates) {
        PawnClass pawn = Board.getPawn(coordinates);
        PawnMoves moves = new PawnMoves(pawn, coordinates);

        if (moves.getPossibleKick().size() > 0) {
            Object[] object = moves.getPossibleKick().toArray();
            return (Coordinates) object[random.nextInt(object.length)];
        } else if (moves.getPossibleMoves().size() > 0) {
            Object[] object = moves.getPossibleMoves().toArray();
            return (Coordinates) object[random.nextInt(object.length)];
        }

        return null;
    }

    private Coordinates chooseMoveHard(Coordinates coordinates) {
        PawnClass pawn = Board.getPawn(coordinates);
        PawnMoves moves = new PawnMoves(pawn, coordinates);

        Object[] object = null;

        if(moves.getPossibleKick().size() > 0) {
            moves.getPossibleKick().forEach(entry -> checkEnemyKickField(entry, pawn));

            if(possibleKickAndNotIsEnemyKickMe.size() > 0) {
                object = possibleKickAndNotIsEnemyKickMe.toArray();
            } else {
                object = moves.getPossibleKick().toArray();
            }

            return (Coordinates) object[random.nextInt(object.length)];
        } else if(moves.getPossibleMoves().size() > 0) {
            object = moves.getPossibleMoves().toArray();
            return (Coordinates) object[random.nextInt(object.length)];
        }

        return null;
    }

    public Coordinates selectRandom(Set<Coordinates> list) {
        Object[] object = list.toArray();
        return (Coordinates) object[random.nextInt(object.length)];
    }

    private void checkEnemyKickField(Coordinates coordinates, PawnClass actualPawn) {
        PawnClass oldPawn = Board.addPawnWithoutDesign(coordinates, actualPawn);

        Set<Coordinates> possibleEnemyKick = new HashSet<>();

        for (Map.Entry<Coordinates, PawnClass> entry : Board.getBoard().entrySet()) {
            if (!Board.isThisSameColor(entry.getKey(), actualPawn.getColor()) && !entry.getValue().getPawn().isKing()) {
                PawnMoves moves = new PawnMoves(entry.getValue(), entry.getKey());
                possibleEnemyKick.addAll(moves.getPossibleKick());
            }
        }

        Board.removePawnWithoutDesign(coordinates);

        if(oldPawn != null) {
            Board.addPawnWithoutDesign(coordinates, oldPawn);
        }

        if(!possibleEnemyKick.contains(coordinates)) {
            possibleKickAndNotIsEnemyKickMe.add(coordinates);
        }
    }
}