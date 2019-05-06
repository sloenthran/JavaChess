package pl.nogacz.chess.pawns;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dawid Nogacz on 06.05.2019
 */
public class PawnColorTest {
    @Test
    public void testValidColors() {
        //Given
        PawnColor black = PawnColor.BLACK;
        PawnColor white = PawnColor.WHITE;

        //When
        boolean blackIsWhite = black.isWhite();
        boolean blackIsBlack = black.isBlack();
        boolean whiteIsWhite = white.isWhite();
        boolean whiteIsBlack = white.isBlack();

        //Then
        assertFalse(blackIsWhite);
        assertTrue(blackIsBlack);
        assertTrue(whiteIsWhite);
        assertFalse(whiteIsBlack);
    }
}