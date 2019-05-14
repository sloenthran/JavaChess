package pl.nogacz.chess.application;

import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;
import pl.nogacz.chess.pawns.PawnColor;
import pl.nogacz.chess.pawns.moves.PawnMoves;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Dawid Nogacz on 05.05.2019
 */
public class GameLogic {
    private HashMap<Coordinates, PawnClass> cacheBoard;
    private PawnColor winner;

    public PawnColor getWinner() {
        return winner;
    }

    public void prepareData() {
        cacheBoard = new HashMap<>(Board.getBoard());
    }

    public boolean isMovePossible() {
        Set<Coordinates> possibleMovesWhite = new HashSet<>();
        Set<Coordinates> possibleMovesBlack = new HashSet<>();
        int pawnWhiteCount = 0;
        int pawnBlackCount = 0;

        for(Map.Entry<Coordinates, PawnClass> entry : cacheBoard.entrySet()) {
            PawnMoves moves = new PawnMoves(entry.getValue(), entry.getKey());

            if(entry.getValue().getColor().isBlack()) {
                pawnBlackCount++;
                possibleMovesBlack.addAll(moves.getPossibleKick());
                possibleMovesBlack.addAll(moves.getPossibleMoves());
            } else {
                pawnWhiteCount++;
                possibleMovesWhite.addAll(moves.getPossibleKick());
                possibleMovesWhite.addAll(moves.getPossibleMoves());
            }
        }

        if(possibleMovesWhite.size() == 0 && possibleMovesBlack.size() == 0 || pawnBlackCount == 1 && pawnWhiteCount == 1) {
            winner = PawnColor.DRAW_COLOR;
            possibleMovesBlack.clear();
            possibleMovesWhite.clear();
        } else if(possibleMovesWhite.size() == 0 || pawnWhiteCount == 1) {
            winner = PawnColor.BLACK;
            possibleMovesBlack.clear();
            possibleMovesWhite.clear();
        } else if(possibleMovesBlack.size() == 0 || pawnBlackCount == 1) {
            winner = PawnColor.WHITE;
            possibleMovesBlack.clear();
            possibleMovesWhite.clear();
        }

        System.out.println(pawnWhiteCount);

        return possibleMovesWhite.size() > 0 && possibleMovesBlack.size() > 0;
    }

    public boolean isKingChecked(PawnColor color) {
        Set<Coordinates> possibleCheck = new HashSet<>();

        for(Map.Entry<Coordinates, PawnClass> entry : cacheBoard.entrySet()) {
            if(entry.getValue().getColor() != color) {
                PawnMoves moves = new PawnMoves(entry.getValue(), entry.getKey());

                possibleCheck.addAll(moves.getPossibleCheck());
            }
        }

        return possibleCheck.size() > 0;
    }

    public Set<Coordinates> getPossiblePawnIfKingIsChecked(PawnColor color) {
        Set<Coordinates> possiblePawn = new HashSet<>();
        Set<Coordinates> possibleCheck = getPossibleCheck(color);

        for(Map.Entry<Coordinates, PawnClass> entry : cacheBoard.entrySet()) {
            if(entry.getValue().getColor() == color) {
                PawnMoves moves = new PawnMoves(entry.getValue(), entry.getKey());

                if(entry.getValue().getPawn().isKing()) {
                    if(moves.getPossibleMoves().size() > 0) {
                        possiblePawn.add(entry.getKey());
                    }
                }

                for(Coordinates coordinates : moves.getPossibleKick()) {
                    if(possibleCheck.contains(coordinates)) {
                        possiblePawn.add(entry.getKey());
                    }
                }
            }
        }

        return possiblePawn;
    }

    private Set<Coordinates> getPossibleCheck(PawnColor color) {
        Set<Coordinates> possibleCheck = new HashSet<>();

        for(Map.Entry<Coordinates, PawnClass> entry : cacheBoard.entrySet()) {
            if(entry.getValue().getColor() != color && !entry.getValue().getPawn().isKing()) {
                PawnMoves moves = new PawnMoves(entry.getValue(), entry.getKey());

                if(moves.getPossibleCheck().size() > 0) {
                    possibleCheck.add(entry.getKey());
                }
            }
        }

        return possibleCheck;
    }

    public Set<Coordinates> getPossibleKickIfKingIsChecked(Coordinates coordinates) {
        PawnClass pawn = Board.getPawn(coordinates);
        PawnMoves moves = new PawnMoves(pawn, coordinates);

        Set<Coordinates> possibleKick = new HashSet<>();
        Set<Coordinates> possibleCheck = getPossibleCheck(pawn.getColor());

        for(Coordinates kickCoordinates : moves.getPossibleKick()) {
            if(possibleCheck.contains(kickCoordinates) && !Board.getPawn(kickCoordinates).getPawn().isKing()) {
                possibleKick.add(kickCoordinates);
            }
        }

        return possibleKick;
    }

    public Set<Coordinates> getPossibleMovesIfKingIsChecked(Coordinates coordinates) {
        PawnClass pawn = Board.getPawn(coordinates);

        if(!pawn.getPawn().isKing()) {
            return null;
        }

        PawnMoves moves = new PawnMoves(pawn, coordinates);

        return moves.getPossibleMoves();
    }
}
