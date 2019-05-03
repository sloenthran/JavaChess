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
    private Set<Coordinates> possibleCheck = new HashSet<>();
    private Set<Coordinates> possibleMovePromote = new HashSet<>();
    private Set<Coordinates> possibleKickPromote = new HashSet<>();
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

        if(y <= 7 && y >= 0) {
            if (!Board.isFieldNotNull(new Coordinates(coordinates.getX(), y))) {
                isPossibleMovePromote(new Coordinates(coordinates.getX(), y), pawn);
                possibleMoves.add(new Coordinates(coordinates.getX(), y));
            }

            checkField(new Coordinates(coordinates.getX() - 1, y), pawn);
            checkField(new Coordinates(coordinates.getX() + 1, y), pawn);
        }

        Board.addPossibleMovePromote(possibleMovePromote);
        Board.addPossibleKickPromote(possibleKickPromote);
    }

    public void isPossibleMovePromote(Coordinates coordinates, PawnClass pawn) {
        if(pawn.getColor().equals(PawnColor.black) && coordinates.getY() == 7) {
            possibleMovePromote.add(coordinates);
        } else if(pawn.getColor().equals(PawnColor.white) && coordinates.getY() == 0) {
            possibleMovePromote.add(coordinates);
        }
    }

    public void isPossibleKickPromote(Coordinates coordinates, PawnClass pawn) {
        if(pawn.getColor().equals(PawnColor.black) && coordinates.getY() == 7) {
            possibleKickPromote.add(coordinates);
        } else if(pawn.getColor().equals(PawnColor.white) && coordinates.getY() == 0) {
            possibleKickPromote.add(coordinates);
        }
    }

    public void checkField(Coordinates coordinates, PawnClass pawn) {
        if (Board.isFieldNotNull(coordinates)) {
            if (!Board.isThisSameColor(coordinates, pawn.getColor())) {
                isPossibleKickPromote(coordinates, pawn);

                if(Board.isKing(coordinates)) {
                    possibleCheck.add(coordinates);
                } else {
                    possibleKick.add(coordinates);
                }
                possibleKick.add(coordinates);
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
