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
        if (possibleMoves.size() > 0) {
            return selectRandom(possibleMoves);
        } else if (possibleKick.size() > 0) {
            return selectRandom(possibleKick);
        }

        return null;
    }

    private Coordinates choosePawnNormal() {
        if (possibleKick.size() > 0) {
            return selectRandom(possibleKick);
        } else if (possibleMoves.size() > 0) {
            return selectRandom(possibleMoves);
        }

        return null;
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

            int point = getMinNumber(cacheMoves, Board.getPawn(coordinates));

            if(point > minNumber) {
                minNumber = point;
            }
        }

        for(Coordinates coordinates : cachePawn) {
            PawnMoves moves = new PawnMoves(Board.getPawn(coordinates), coordinates);

            Set<Coordinates> cacheMoves = new HashSet<>();
            cacheMoves.addAll(moves.getPossibleKick());
            cacheMoves.addAll(moves.getPossibleMoves());

            cachePossiblePawn.addAll(getListWithOnlyMinNumber(cacheMoves, Board.getPawn(coordinates), minNumber));
        }

        return selectRandom(cachePossiblePawn);
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
            return selectRandom(moves.getPossibleMoves());
        } else if (moves.getPossibleKick().size() > 0) {
            return selectRandom(moves.getPossibleKick());
        }

        return null;
    }

    private Coordinates chooseMoveNormal(Coordinates coordinates) {
        PawnClass pawn = Board.getPawn(coordinates);
        PawnMoves moves = new PawnMoves(pawn, coordinates);

        if (moves.getPossibleKick().size() > 0) {
            return selectRandom(moves.getPossibleKick());
        } else if (moves.getPossibleMoves().size() > 0) {
            return selectRandom(moves.getPossibleMoves());
        }

        return null;
    }

    private Coordinates chooseMoveHard(Coordinates coordinates) {
        PawnClass pawn = Board.getPawn(coordinates);
        PawnMoves moves = new PawnMoves(pawn, coordinates);

        Set<Coordinates> possibleMove = new HashSet<>();
        possibleMove.addAll(moves.getPossibleMoves());
        possibleMove.addAll(moves.getPossibleKick());

        int minNumber = getMinNumber(possibleMove, pawn);

        Set<Coordinates> test = getListWithOnlyMinNumber(possibleMove, pawn, minNumber);

        return selectRandom(test);
    }

    private int getMinNumber(Set<Coordinates> list, PawnClass actualPawn) {
        int minNumber = -10000;

        for(Coordinates coordinates : list) {
            PawnClass oldPawn = Board.addPawnWithoutDesign(coordinates, actualPawn);

            int point = boardPoint.calculateBoard();

            if(point > minNumber) {
                minNumber = point;
            }

            Board.removePawnWithoutDesign(coordinates);

            if (oldPawn != null) {
                Board.addPawnWithoutDesign(coordinates, oldPawn);
            }
        }

        return minNumber;
    }

    private Set<Coordinates> getListWithOnlyMinNumber(Set<Coordinates> list, PawnClass actualPawn, int minNumber) {
        Set<Coordinates> returnList = new HashSet<>();

        for(Coordinates coordinates : list) {
            PawnClass oldPawn = Board.addPawnWithoutDesign(coordinates, actualPawn);

            int point = boardPoint.calculateBoard();

            if(point == minNumber) {
                returnList.add(coordinates);
            }

            Board.removePawnWithoutDesign(coordinates);

            if (oldPawn != null) {
                Board.addPawnWithoutDesign(coordinates, oldPawn);
            }
        }

        return returnList;
    }

    public Coordinates selectRandom(Set<Coordinates> list) {
        Object[] object = list.toArray();
        return (Coordinates) object[random.nextInt(object.length)];
    }
}