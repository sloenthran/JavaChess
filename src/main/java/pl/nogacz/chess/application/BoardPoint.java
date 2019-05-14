package pl.nogacz.chess.application;

import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.Pawn;
import pl.nogacz.chess.pawns.PawnClass;
import pl.nogacz.chess.pawns.moves.PawnMoves;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dawid Nogacz on 14.05.2019
 */
public class BoardPoint {
    public int calculateBoard() {
        HashMap<Coordinates, PawnClass> cacheBoard = new HashMap<>(Board.getBoard());

        int whitePoint = 0;
        int blackPoint = 0;

        for(Map.Entry<Coordinates, PawnClass> entry : cacheBoard.entrySet()) {
            PawnMoves pawnMoves = new PawnMoves(entry.getValue(), entry.getKey());

            if(entry.getValue().getColor().isWhite()) {
                if(pawnMoves.getPossibleCheck().size() > 0) {
                    whitePoint += 100;
                    blackPoint -= 100;
                } else if(pawnMoves.getPossibleKick().size() > 0) {
                    whitePoint += 20;
                }

                whitePoint += calculatePawn(entry.getKey(), entry.getValue());
            } else {
                if(pawnMoves.getPossibleCheck().size() > 0) {
                    blackPoint += 100;
                    whitePoint -= 100;
                } else if(pawnMoves.getPossibleKick().size() > 0) {
                    blackPoint += 20;
                }

                blackPoint += calculatePawn(entry.getKey(), entry.getValue());
            }
        }

        return blackPoint - whitePoint;
    }

    private int calculatePawn(Coordinates coordinates, PawnClass pawn) {
        int point = 1;

        if(pawn.getColor().isWhite()) {
            point++;
        } else if(pawn.getColor().isBlack() && pawn.getPawn() == Pawn.PAWN && coordinates.getY() < 4) {
            point++;
        }

        switch(pawn.getPawn()) {
            case QUEEN: point += 7; break;
            case KNIGHT: point += 3; break;
            case ROOK: point += 2; break;
            case BISHOP: point += 2; break;
        }

        return point;
    }
}
