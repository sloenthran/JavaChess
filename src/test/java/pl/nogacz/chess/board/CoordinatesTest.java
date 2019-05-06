package pl.nogacz.chess.board;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dawid Nogacz on 06.05.2019
 */
public class CoordinatesTest {
    @Test
    public void testFunction() {
        //Given
        Coordinates coordinates = new Coordinates(8, 6);

        //When
        int x = coordinates.getX();
        int y = coordinates.getY();
        boolean isValid = coordinates.isValid();

        //Then
        assertEquals(x, 8);
        assertEquals(y, 6);
        assertFalse(isValid);
    }
}