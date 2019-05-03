package pl.nogacz.chess.pawns.moves;

import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class Knight implements PawnMovesInterface {
    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possibleCheck = new HashSet<>();
    private Coordinates coordinates;

    @Override
    public void getPawnCoordinate(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public void checkPossibleMoves() {
        checkCoordinates(coordinates.getX() + 1, coordinates.getY() - 2);
        checkCoordinates(coordinates.getX() - 1, coordinates.getY() - 2);
        checkCoordinates(coordinates.getX() + 1, coordinates.getY() + 2);
        checkCoordinates(coordinates.getX() - 1, coordinates.getY() + 2);
        checkCoordinates(coordinates.getX() + 2, coordinates.getY() - 1);
        checkCoordinates(coordinates.getX() - 2, coordinates.getY() - 1);
        checkCoordinates(coordinates.getX() + 2, coordinates.getY() + 1);
        checkCoordinates(coordinates.getX() - 2, coordinates.getY() + 1);
    }

    private void checkCoordinates(int x, int y) {
        if(x <= 7 && x >= 0 && y <= 7 && y >= 0) {
            if(Board.isFieldNotNull(new Coordinates(x, y))) {
                PawnClass pawn = Board.getPawn(coordinates);

                if(!Board.isThisSameColor(new Coordinates(x, y), pawn.getColor())) {
                    if(Board.isKing(new Coordinates(x, y))) {
                        possibleCheck.add(new Coordinates(x, y));
                    } else {
                        possibleKick.add(new Coordinates(x, y));
                    }
                }
            } else {
                possibleMoves.add(new Coordinates(x, y));
            }
        }
    }

    @Override
    public Set<Coordinates> getPossibleMoves() {
        return possibleMoves;
    }

    @Override
    public Set<Coordinates> getPossibleKick() {
        return possibleKick;
    }

    @Override
    public Set<Coordinates> getPossibleCheck() {
        return possibleCheck;
    }
}
