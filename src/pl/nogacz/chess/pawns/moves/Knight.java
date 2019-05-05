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
    private PawnClass actualPawn;

    @Override
    public void getPawnCoordinate(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public void checkPossibleMoves() {
        actualPawn = Board.getPawn(coordinates);

        checkCoordinates(new Coordinates(coordinates.getX() + 1, coordinates.getY() - 2));
        checkCoordinates(new Coordinates(coordinates.getX() - 1, coordinates.getY() - 2));
        checkCoordinates(new Coordinates(coordinates.getX() + 1, coordinates.getY() + 2));
        checkCoordinates(new Coordinates(coordinates.getX() - 1, coordinates.getY() + 2));
        checkCoordinates(new Coordinates(coordinates.getX() + 2, coordinates.getY() - 1));
        checkCoordinates(new Coordinates(coordinates.getX() - 2, coordinates.getY() - 1));
        checkCoordinates(new Coordinates(coordinates.getX() + 2, coordinates.getY() + 1));
        checkCoordinates(new Coordinates(coordinates.getX() - 2, coordinates.getY() + 1));
    }

    private void checkCoordinates(Coordinates coordinates) {
        if(coordinates.isValid()) {
            if(Board.isFieldNotNull(coordinates)) {
                PawnClass pawn = Board.getPawn(coordinates);

                if(!Board.isThisSameColor(coordinates, actualPawn.getColor())) {
                    if(pawn.getPawn().isKing()) {
                        possibleCheck.add(coordinates);
                    } else {
                        possibleKick.add(coordinates);
                    }
                }
            } else {
                possibleMoves.add(coordinates);
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
