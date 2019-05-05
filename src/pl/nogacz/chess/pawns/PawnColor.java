package pl.nogacz.chess.pawns;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public enum PawnColor {
    black,
    white;

    public boolean isBlack() {
        return this == black;
    }

    public boolean isWhite() {
        return this == white;
    }
}
