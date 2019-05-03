package pl.nogacz.chess.pawns.moves;

import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class Bishop implements PawnMovesInterface {
    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();

    private Set<Coordinates> possibleCheck = new HashSet<>();
    private boolean checked = false;

    private Coordinates coordinates;

    @Override
    public void getPawnCoordinate(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public void checkPossibleMoves() {
        boolean checkUpLeft = true;
        boolean checkUpRight = true;
        boolean checkBottomLeft = true;
        boolean checkBottomRight = true;

        for(int i = 1; i < 8; i++) {
            if(checkUpLeft) {
                checkUpLeft = checkCoordinates(coordinates.getX() - i, coordinates.getY() - i);
            }

            if(checkUpRight) {
                checkUpRight = checkCoordinates(coordinates.getX() + i, coordinates.getY() - i);
            }

            if(checkBottomLeft) {
                checkBottomLeft = checkCoordinates(coordinates.getX() - i, coordinates.getY() + i);
            }

            if(checkBottomRight) {
                checkBottomRight = checkCoordinates(coordinates.getX() + i, coordinates.getY() + i);
            }
        }
    }

    private boolean checkCoordinates(int x, int y) {
        if(x <= 7 && x >= 0 && y <= 7 && y >= 0) {
            if(Board.isFieldNotNull(new Coordinates(x, y))) {
                PawnClass pawn = Board.getPawn(coordinates);

                if(!Board.isThisSameColor(new Coordinates(x, y), pawn.getColor())) {
                    if(Board.isKing(new Coordinates(x, y))) {
                        possibleCheck.add(new Coordinates(x, y));
                        checked = true;
                        return true;
                    } else {
                        possibleKick.add(new Coordinates(x, y));
                    }
                }
            } else {
                if(checked) {
                    if(!Board.isFieldNotNull(new Coordinates(x, y))) {
                        possibleCheck.add(new Coordinates(x, y));
                    } else {
                        return false;
                    }
                } else {
                    possibleMoves.add(new Coordinates(x, y));
                }

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

    @Override
    public Set<Coordinates> getPossibleCheck() {
        return possibleCheck;
    }
}
