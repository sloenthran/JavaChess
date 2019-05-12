package pl.nogacz.chess.pawns.moves;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.Pawn;
import pl.nogacz.chess.pawns.PawnClass;
import pl.nogacz.chess.pawns.PawnColor;

import java.util.Set;

/**
 * @author Dawid Nogacz on 05.05.2019
 */
@RunWith(PowerMockRunner.class)
public class KingTest {
    @PrepareForTest({Board.class})
    @Test
    public void getPossibleMovesIfFieldIsEmpty() {
        //Given
        PowerMockito.mockStatic(Board.class);

        Coordinates coordinates = new Coordinates(4, 6);
        PawnClass pawnClass = new PawnClass(Pawn.KING, PawnColor.WHITE);
        King pawn = new King();
        pawn.getPawnCoordinate(coordinates);

        //When
        PowerMockito.when(Board.getPawn(coordinates)).thenReturn(pawnClass);

        PowerMockito.when(Board.isFieldNotNull(new Coordinates(5, 5))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(5, 6))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(4, 5))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(4, 7))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(5, 7))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 5))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 6))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 7))).thenReturn(false);

        pawn.checkPossibleMoves();

        Set<Coordinates> possibleMoves = pawn.getPossibleMoves();

        //Then
        Assert.assertEquals(8, possibleMoves.size());
    }

    @PrepareForTest({Board.class})
    @Test
    public void getPossibleMovesIfFieldIsFull() {
        //Given
        PowerMockito.mockStatic(Board.class);

        Coordinates coordinates = new Coordinates(4, 6);
        PawnClass pawnClass = new PawnClass(Pawn.KING, PawnColor.WHITE);
        PawnClass enemyPawn = new PawnClass(Pawn.PAWN, PawnColor.BLACK);
        King pawn = new King();
        pawn.getPawnCoordinate(coordinates);

        //When
        PowerMockito.when(Board.getPawn(coordinates)).thenReturn(pawnClass);

        PowerMockito.when(Board.isFieldNotNull(new Coordinates(5, 5))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(5, 6))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(4, 5))).thenReturn(true);
        PowerMockito.when(Board.getPawn(new Coordinates(4, 5))).thenReturn(enemyPawn);
        PowerMockito.when(Board.isThisSameColor(new Coordinates(4, 5), PawnColor.WHITE)).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(4, 7))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(5, 7))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 5))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 6))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 7))).thenReturn(false);

        pawn.checkPossibleMoves();

        Set<Coordinates> possibleMoves = pawn.getPossibleMoves();

        //Then
        Assert.assertEquals(7, possibleMoves.size());
    }

    @PrepareForTest({Board.class})
    @Test
    public void getPossibleKickIfFieldIsEmpty() {
        //Given
        PowerMockito.mockStatic(Board.class);

        Coordinates coordinates = new Coordinates(4, 6);
        PawnClass pawnClass = new PawnClass(Pawn.KING, PawnColor.WHITE);
        King pawn = new King();
        pawn.getPawnCoordinate(coordinates);

        //When
        PowerMockito.when(Board.getPawn(coordinates)).thenReturn(pawnClass);

        PowerMockito.when(Board.isFieldNotNull(new Coordinates(5, 5))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(5, 6))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(4, 5))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(4, 7))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(5, 7))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 5))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 6))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 7))).thenReturn(false);

        pawn.checkPossibleMoves();

        Set<Coordinates> possibleKick = pawn.getPossibleKick();

        //Then
        Assert.assertEquals(0, possibleKick.size());
    }

    @PrepareForTest({Board.class})
    @Test
    public void getPossibleKickIfFieldIsFull() {
        //Given
        PowerMockito.mockStatic(Board.class);

        Coordinates coordinates = new Coordinates(4, 6);
        PawnClass pawnClass = new PawnClass(Pawn.KING, PawnColor.WHITE);
        PawnClass enemyPawn = new PawnClass(Pawn.PAWN, PawnColor.BLACK);
        King pawn = new King();
        pawn.getPawnCoordinate(coordinates);

        //When
        PowerMockito.when(Board.getPawn(coordinates)).thenReturn(pawnClass);

        PowerMockito.when(Board.isFieldNotNull(new Coordinates(5, 5))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(5, 6))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(4, 5))).thenReturn(true);
        PowerMockito.when(Board.getPawn(new Coordinates(4, 5))).thenReturn(enemyPawn);
        PowerMockito.when(Board.isThisSameColor(new Coordinates(4, 5), PawnColor.WHITE)).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(4, 7))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(5, 7))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 5))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 6))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 7))).thenReturn(false);

        pawn.checkPossibleMoves();

        Set<Coordinates> possibleKick = pawn.getPossibleKick();

        //Then
        Assert.assertEquals(1, possibleKick.size());
    }
}