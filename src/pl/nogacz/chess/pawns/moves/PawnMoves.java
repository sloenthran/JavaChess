package pl.nogacz.chess.pawns.moves;

import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.Pawn;
import pl.nogacz.chess.pawns.PawnClass;

import java.util.Set;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class PawnMoves {
    private PawnMovesInterface pawnMoves;
    private PawnClass pawn;

    public PawnMoves(PawnClass pawn, Coordinates coordinates) {
        this.pawn = pawn;

        pawnMoves = getMoveClass();
        pawnMoves.getPawnCoordinate(coordinates);
        pawnMoves.checkPossibleMoves();
    }

    public Set<Coordinates> getPossibleMoves() {
        return pawnMoves.getPossibleMoves();
    }

    public Set<Coordinates> getPossibleKick() {
        return pawnMoves.getPossibleKick();
    }

    public Set<Coordinates> getPossibleCheck() { return pawnMoves.getPossibleCheck(); }

    public Pawn getPawn() {
        return pawn.getPawn();
    }

    private PawnMovesInterface getMoveClass() {
        String className = "pl.nogacz.chess.pawns.moves." + pawn.getPawn();
        PawnMovesInterface moveInterface = null;

        try {
            moveInterface = (PawnMovesInterface) Class.forName(className).newInstance();
            return moveInterface;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        return moveInterface;
    }
}
