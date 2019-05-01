package pl.nogacz.chess.pawns.moves;

import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.Pawn;
import pl.nogacz.chess.pawns.PawnClass;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class Queen implements PawnMovesInterface{
    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();
    private Coordinates coordinates;
    private PawnClass pawn;

    @Override
    public void getPawnCoordinate(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public void checkPossibleMoves() {
        pawn = Board.getPawn(coordinates);

        addRookMoves();
        addBishopMoves();
    }

    private void addRookMoves() {
        PawnMoves moves = new PawnMoves(new PawnClass(Pawn.Rook, pawn.getColor()), coordinates);

        possibleMoves.addAll(moves.getPossibleMoves());
        possibleKick.addAll(moves.getPossibleKick());
    }

    private void addBishopMoves() {
        PawnMoves moves = new PawnMoves(new PawnClass(Pawn.Bishop, pawn.getColor()), coordinates);

        possibleMoves.addAll(moves.getPossibleMoves());
        possibleKick.addAll(moves.getPossibleKick());
    }

    @Override
    public Set<Coordinates> getPossibleMoves() {
        return possibleMoves;
    }

    @Override
    public Set<Coordinates> getPossibleKick() {
        return possibleKick;
    }
}
