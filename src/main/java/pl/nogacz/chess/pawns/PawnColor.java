package pl.nogacz.chess.pawns;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public enum PawnColor {
    BLACK,
    WHITE;

    public boolean isBlack() {
        return this == BLACK;
    }

    public boolean isWhite() {
        return this == WHITE;
    }
}
