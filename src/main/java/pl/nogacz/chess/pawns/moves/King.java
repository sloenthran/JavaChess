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
    private Set<Coordinates> possibleCheck = new HashSet<>();
    private Coordinates actualCoordinates;
    private PawnClass actualPawn;

    @Override
    public void getPawnCoordinate(Coordinates coordinates) {
        this.actualCoordinates = coordinates;
    }

    @Override
    public void checkPossibleMoves() {
        actualPawn = Board.getPawn(actualCoordinates);

        checkCoordinates(actualCoordinates);
        checkCoordinates(new Coordinates(actualCoordinates.getX() + 1, actualCoordinates.getY()));
        checkCoordinates(new Coordinates(actualCoordinates.getX() - 1, actualCoordinates.getY()));
        checkCoordinates(new Coordinates(actualCoordinates.getX(), actualCoordinates.getY() + 1));
        checkCoordinates(new Coordinates(actualCoordinates.getX(), actualCoordinates.getY() - 1));
        checkCoordinates(new Coordinates(actualCoordinates.getX() + 1, actualCoordinates.getY() + 1));
        checkCoordinates(new Coordinates(actualCoordinates.getX() - 1, actualCoordinates.getY() - 1));
        checkCoordinates(new Coordinates(actualCoordinates.getX() + 1, actualCoordinates.getY() - 1));
        checkCoordinates(new Coordinates(actualCoordinates.getX() - 1, actualCoordinates.getY() + 1));
    }

    private void checkCoordinates(Coordinates coordinates) {
        if(coordinates.isValid()) {
            if(!isEnemyKickField(coordinates)) {
                if(Board.isFieldNotNull(coordinates)) {
                    PawnClass pawn = Board.getPawn(coordinates);

                    if(!Board.isThisSameColor(coordinates, actualPawn.getColor())) {
                        if(pawn.getPawn().isKing()) {
                            possibleCheck.add(coordinates);
                        } else {
                            possibleKick.add(coordinates);
                        }
                    }
                } else if(coordinates != actualCoordinates) {
                    possibleMoves.add(coordinates);
                }
            }
        }
    }

    private boolean isEnemyKickField(Coordinates coordinates) {
        PawnClass oldPawn = Board.addPawnWithoutDesign(coordinates, actualPawn);

        for(Map.Entry<Coordinates, PawnClass> entry : Board.getBoard().entrySet()) {
            if(!Board.isThisSameColor(entry.getKey(), actualPawn.getColor()) && !entry.getValue().getPawn().isKing()) {
                PawnMoves moves = new PawnMoves(entry.getValue(), entry.getKey());
                possibleEnemyKick.addAll(moves.getPossibleKick());
                possibleEnemyKick.addAll(moves.getPossibleCheck());
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

    @Override
    public Set<Coordinates> getPossibleCheck() {
        return possibleCheck;
    }
}
