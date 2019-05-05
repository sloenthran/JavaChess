package pl.nogacz.chess.application;

import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;
import pl.nogacz.chess.pawns.PawnColor;
import pl.nogacz.chess.pawns.moves.PawnMoves;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Dawid Nogacz on 05.05.2019
 */
public class GameLogic {
    private HashMap<Coordinates, PawnClass> cacheBoard;
    private PawnColor winner;

    public PawnColor getWinner() {
        return winner;
    }

    public void prepareData() {
        cacheBoard = new HashMap<>(Board.getBoard());
    }

    public boolean isMovePossible() {
        Set<Coordinates> possibleMovesWhite = new HashSet<>();
        Set<Coordinates> possibleMovesBlack = new HashSet<>();

        for(Map.Entry<Coordinates, PawnClass> entry : cacheBoard.entrySet()) {
            PawnMoves moves = new PawnMoves(entry.getValue(), entry.getKey());

            if(entry.getValue().getColor().isBlack()) {
                possibleMovesBlack.addAll(moves.getPossibleKick());
                possibleMovesBlack.addAll(moves.getPossibleMoves());
            } else {
                possibleMovesWhite.addAll(moves.getPossibleKick());
                possibleMovesWhite.addAll(moves.getPossibleMoves());
            }
        }

        if(possibleMovesWhite.size() == 0) {
            winner = PawnColor.black;
        } else if(possibleMovesBlack.size() == 0) {
            winner = PawnColor.white;
        }

        return possibleMovesWhite.size() > 0 && possibleMovesBlack.size() > 0;
    }
}
