package pl.nogacz.chess.pawns;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public enum Pawn {
    PAWN,
    ROOK,
    KNIGHT,
    BISHOP,
    QUEEN,
    KING;

    public boolean isKing() {
        return this == KING;
    }
}