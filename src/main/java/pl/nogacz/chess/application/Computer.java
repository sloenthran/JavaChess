package pl.nogacz.chess.application;


import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;

import pl.nogacz.chess.pawns.moves.PawnMoves;

import java.util.*;



/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class Computer {
    private HashMap<Coordinates, PawnClass> cacheBoard;
    private Random random = new Random();
    private static int skill = 0; // 0 - Normal || 1 - Easy || 2 - Hard

    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKickAndNotIsEnemyKickMe = new HashSet<>();

    private BoardPoint boardPoint = new BoardPoint();

    public Computer() {

    }

    public static int getSkill(){
        return skill;
    }


    public static void setSkill(int s) {
        skill = s;
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

            for(Coordinates moveCoordinates : cacheMoves) {
                PawnClass oldPawn = Board.addPawnWithoutDesign(moveCoordinates, Board.getPawn(coordinates));

                int point = boardPoint.calculateBoard();

                if (point == minNumber) {
                    cachePossiblePawn.add(coordinates);
                }

                Board.removePawnWithoutDesign(moveCoordinates);

                if (oldPawn != null) {
                    Board.addPawnWithoutDesign(moveCoordinates, oldPawn);
                }
            }
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

        Set<Coordinates> listWithOnlyMinNumber = getListWithOnlyMinNumber(possibleMove, pawn);

        listWithOnlyMinNumber.forEach(entry -> checkEnemyKickField(entry, pawn));

        if(possibleKickAndNotIsEnemyKickMe.size() > 0) {
            return selectRandom(possibleKickAndNotIsEnemyKickMe);
        } else {
            return selectRandom(listWithOnlyMinNumber);
        }
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

    private Set<Coordinates> getListWithOnlyMinNumber(Set<Coordinates> list, PawnClass actualPawn) {
        int minNumber = getMinNumber(list, actualPawn);
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

    public Coordinates selectRandom(Set<Coordinates> list) {
        Object[] object = list.toArray();
        return (Coordinates) object[random.nextInt(object.length)];
    }
}