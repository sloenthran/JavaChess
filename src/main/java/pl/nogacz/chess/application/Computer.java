package pl.nogacz.chess.application;

import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.Pawn;
import pl.nogacz.chess.pawns.PawnClass;
import pl.nogacz.chess.pawns.PawnColor;
import pl.nogacz.chess.pawns.moves.PawnMoves;

import java.util.*;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class Computer {
    private HashMap<Coordinates, PawnClass> cacheBoard;
    private Random random = new Random();

    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possibleMoves = new HashSet<>();

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
        Object[] object = null;

        if(possibleKick.size() > 0) {
            object = possibleKick.toArray();
        } else if(possibleMoves.size() > 0) {
            object = possibleMoves.toArray();

        }

        return (Coordinates) object[random.nextInt(object.length)];
    }

    public Coordinates chooseMove(Coordinates coordinates) {
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