package pl.nogacz.chess.pawns.moves;

import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.Pawn;
import pl.nogacz.chess.pawns.PawnClass;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class King implements PawnMovesInterface {
    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possibleEnemyKick = new HashSet<>();
    private Coordinates coordinates;
    private PawnClass pawn;

    @Override
    public void getPawnCoordinate(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public void checkPossibleMoves() {
        pawn = Board.getPawn(coordinates);

        checkCoordinates(coordinates.getX() + 1, coordinates.getY());
        checkCoordinates(coordinates.getX() - 1, coordinates.getY());
        checkCoordinates(coordinates.getX(), coordinates.getY() + 1);
        checkCoordinates(coordinates.getX(), coordinates.getY() - 1);
        checkCoordinates(coordinates.getX() + 1, coordinates.getY() + 1);
        checkCoordinates(coordinates.getX() - 1, coordinates.getY() - 1);
        checkCoordinates(coordinates.getX() + 1, coordinates.getY() - 1);
        checkCoordinates(coordinates.getX() - 1, coordinates.getY() + 1);
    }

    //TODO Król musi uwzględniać że nie może zostać zbity...
    private boolean checkCoordinates(int x, int y) {
        if(x <= 7 && x >= 0 && y <= 7 && y >= 0) {
            if(!isEnemyKickField(new Coordinates(x, y))) {
                if (Board.isFieldNotNull(new Coordinates(x, y))) {
                    PawnClass pawn = Board.getPawn(coordinates);

                    if (!Board.isThisSameColor(new Coordinates(x, y), pawn.getColor())) {
                        possibleKick.add(new Coordinates(x, y));
                    }
                } else {
                    possibleMoves.add(new Coordinates(x, y));
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isEnemyKickField(Coordinates coordinates) {
        PawnClass oldPawn = Board.addPawnWithoutDesign(coordinates, pawn);

        for(Map.Entry<Coordinates, PawnClass> entry : Board.getBoard().entrySet()) {
            if(!Board.isThisSameColor(entry.getKey(), pawn.getColor()) && !entry.getValue().getPawn().equals(Pawn.King)) {
                PawnMoves moves = new PawnMoves(entry.getValue(), entry.getKey());
                possibleEnemyKick.addAll(moves.getPossibleKick());
            }
        }

        Board.removePawnWithoutDesign(coordinates);

        if(oldPawn != null) {
            Board.addPawnWithoutDesign(coordinates, oldPawn);
        }

        if(possibleEnemyKick.contains(coordinates)) {
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
}
