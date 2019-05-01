package pl.nogacz.chess.pawns.moves;

import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class King implements PawnMovesInterface {
    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();
    private Coordinates coordinates;

    @Override
    public void getPawnCoordinate(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public void checkPossibleMoves() {
        checkCoordinates(coordinates.getX() + 1, coordinates.getY());
        checkCoordinates(coordinates.getX() - 1, coordinates.getY());
        checkCoordinates(coordinates.getX(), coordinates.getY() + 1);
        checkCoordinates(coordinates.getX(), coordinates.getY() - 1);
        checkCoordinates(coordinates.getX() + 1, coordinates.getY() + 1);
        checkCoordinates(coordinates.getX() - 1, coordinates.getY() - 1);
        checkCoordinates(coordinates.getX() + 1, coordinates.getY() - 1);
        checkCoordinates(coordinates.getX() - 1, coordinates.getY() + 1);
    }

    private boolean checkCoordinates(int x, int y) {
        if(x <= 7 && x >= 0 && y <= 7 && y >= 0) {
            if(Board.isFieldNotNull(new Coordinates(x, y))) {
                PawnClass pawn = Board.getPawn(coordinates);

                if(!Board.isThisSameColor(new Coordinates(x, y), pawn.getColor())) {
                    possibleKick.add(new Coordinates(x, y));
                }
            } else {
                possibleMoves.add(new Coordinates(x, y));
                return true;
            }
        }

        return false;
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
