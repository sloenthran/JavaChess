package pl.nogacz.chess.board;

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
    private Set<Coordinates> possibleMoves = new HashSet<>();

    public void getGameData() {
        cacheBoard = new HashMap<>(Board.getBoard());

        possibleMoves.clear();

        for(Map.Entry<Coordinates, PawnClass> entry : cacheBoard.entrySet()) {
            if(entry.getValue().getColor().equals(PawnColor.black)) {
                PawnMoves moves = new PawnMoves(entry.getValue(), entry.getKey());
                if(moves.getPossibleMoves().size() > 0 || moves.getPossibleKick().size() > 0) {
                    possibleMoves.add(entry.getKey());
                }
            }
        }
    }

    public Coordinates choosePawn() {
        Object[] object = possibleMoves.toArray();
        return (Coordinates) object[random.nextInt(object.length)];
    }

    public Coordinates chooseMove(Coordinates coordinates) {
        PawnClass pawn = Board.getPawn(coordinates);
        PawnMoves moves = new PawnMoves(pawn, coordinates);

        if(moves.getPossibleKick().size() > 0) {
            Object[] object = moves.getPossibleKick().toArray();
            return (Coordinates) object[random.nextInt(object.length)];
        } else {
            Object[] object = moves.getPossibleMoves().toArray();
            return (Coordinates) object[random.nextInt(object.length)];
        }
    }
}