package pl.nogacz.chess.pawns.moves;

import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class Rook implements PawnMovesInterface{
    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();

    private Set<Coordinates> possibleCheck = new HashSet<>();
    private boolean kingChecked = false;

    private Coordinates coordinates;
    private PawnClass actualPawn;

    @Override
    public void getPawnCoordinate(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public void checkPossibleMoves() {
        actualPawn = Board.getPawn(coordinates);

        boolean checkUpX = true;
        boolean checkUpY = true;
        boolean checkBottomX = true;
        boolean checkBottomY = true;

        for(int i = 1; i < 8; i++) {
            if(checkUpX) {
                checkUpX = checkCoordinates(new Coordinates(coordinates.getX() + i, coordinates.getY()));
            }
        }

        kingChecked = false;

        for(int i = 1; i < 8; i++) {
            if(checkUpY) {
                checkUpY = checkCoordinates(new Coordinates(coordinates.getX(), coordinates.getY() + i));
            }
        }

        kingChecked = false;

        for(int i = 1; i < 8; i++) {
            if(checkBottomX) {
                checkBottomX = checkCoordinates(new Coordinates(coordinates.getX() - i, coordinates.getY()));
            }
        }

        kingChecked = false;

        for(int i = 1; i < 8; i++) {
            if(checkBottomY) {
                checkBottomY = checkCoordinates(new Coordinates(coordinates.getX(), coordinates.getY() - i));
            }
        }
    }

    private boolean checkCoordinates(Coordinates coordinates) {
        if(!coordinates.isValid()) {
            return false;
        }

        if(Board.isFieldNotNull(coordinates)) {
            PawnClass enemyPawn = Board.getPawn(coordinates);

            if(!Board.isThisSameColor(coordinates, actualPawn.getColor()) && !kingChecked) {
                if(enemyPawn.getPawn().isKing()) {
                    kingChecked = true;
                    possibleCheck.add(coordinates);

                    return true;
                } else {
                    possibleKick.add(coordinates);
                }
            }
        } else {
            if(kingChecked) {
                possibleCheck.add(coordinates);
            } else {
                possibleMoves.add(coordinates);
            }

            return true;
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

    @Override
    public Set<Coordinates> getPossibleCheck() {
        return possibleCheck;
    }
}
