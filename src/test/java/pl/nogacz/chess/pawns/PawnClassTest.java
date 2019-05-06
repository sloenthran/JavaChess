package pl.nogacz.chess.pawns;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dawid Nogacz on 06.05.2019
 */
public class PawnClassTest {
    @Test
    public void testFunction() {
        //Given
        PawnClass pawnClass = new PawnClass(Pawn.King, PawnColor.white);

        //When
        Pawn getPawn = pawnClass.getPawn();
        PawnColor getColor = pawnClass.getColor();

        //Then
        assertEquals(Pawn.King, getPawn);
        assertEquals(PawnColor.white, getColor);
    }
}