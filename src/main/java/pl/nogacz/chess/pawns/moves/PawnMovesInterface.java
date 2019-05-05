package pl.nogacz.chess.pawns.moves;

import pl.nogacz.chess.board.Coordinates;

import java.util.Set;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public interface PawnMovesInterface {
    void getPawnCoordinate(Coordinates coordinates);
    void checkPossibleMoves();
    Set<Coordinates> getPossibleMoves();
    Set<Coordinates> getPossibleKick();
    Set<Coordinates> getPossibleCheck();
}
