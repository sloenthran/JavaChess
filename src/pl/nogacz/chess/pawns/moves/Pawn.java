package pl.nogacz.chess.pawns.moves;

import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;
import pl.nogacz.chess.pawns.PawnColor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class Pawn implements PawnMovesInterface{
    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();
    private Coordinates coordinates;

    @Override
    public void getPawnCoordinate(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public void checkPossibleMoves() {
        PawnClass pawn = Board.getPawn(coordinates);
        int y = 0;

        if(pawn.getColor().equals(PawnColor.black)) {
            y = coordinates.getY() + 1;
        } else {
            y = coordinates.getY() - 1;
        }

        if(!Board.isFieldNotNull(new Coordinates(coordinates.getX(), y))) {
            possibleMoves.add(new Coordinates(coordinates.getX(), y));
        } else {
            if(Board.isFieldNotNull(new Coordinates(coordinates.getX() - 1, y))) {
                if(!Board.isThisSameColor(new Coordinates(coordinates.getX() - 1, y), pawn.getColor())) {
                    possibleKick.add(new Coordinates(coordinates.getX() - 1, y));
                }
            }

            if(Board.isFieldNotNull(new Coordinates(coordinates.getX() + 1, y))) {
                if(!Board.isThisSameColor(new Coordinates(coordinates.getX() + 1, y), pawn.getColor())) {
                    possibleKick.add(new Coordinates(coordinates.getX() + 1, y));
                }
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
}
