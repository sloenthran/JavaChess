package pl.nogacz.chess.pawns;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dawid Nogacz on 06.05.2019
 */
public class PawnTest {
    @Test
    public void isKing() {
        //Given
        Pawn queen = Pawn.Queen;
        Pawn king = Pawn.King;

        //When
        boolean queenIsKing = queen.isKing();
        boolean kingIsKing = king.isKing();

        //Then
        assertFalse(queenIsKing);
        assertTrue(kingIsKing);
    }
}