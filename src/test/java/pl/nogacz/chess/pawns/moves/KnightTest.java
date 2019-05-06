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
public class KnightTest {
    @PrepareForTest({Board.class})
    @Test
    public void getPossibleMovesIfFieldIsEmpty() {
        //Given
        PowerMockito.mockStatic(Board.class);

        Coordinates coordinates = new Coordinates(1, 7);
        PawnClass pawnClass = new PawnClass(Pawn.KNIGHT, PawnColor.WHITE);
        Knight pawn = new Knight();
        pawn.getPawnCoordinate(coordinates);

        //When
        PowerMockito.when(Board.getPawn(coordinates)).thenReturn(pawnClass);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(2, 5))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 6))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(0, 5))).thenReturn(false);

        pawn.checkPossibleMoves();

        Set<Coordinates> possibleMoves = pawn.getPossibleMoves();

        //Then
        Assert.assertEquals(3, possibleMoves.size());
    }

    @PrepareForTest({Board.class})
    @Test
    public void getPossibleMovesIfFieldIsFull() {
        //Given
        PowerMockito.mockStatic(Board.class);

        Coordinates coordinates = new Coordinates(1, 7);
        PawnClass pawnClass = new PawnClass(Pawn.KNIGHT, PawnColor.WHITE);
        PawnClass enemyPawn = new PawnClass(Pawn.QUEEN, PawnColor.BLACK);
        Knight pawn = new Knight();
        pawn.getPawnCoordinate(coordinates);

        //When
        PowerMockito.when(Board.getPawn(coordinates)).thenReturn(pawnClass);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(2, 5))).thenReturn(false);
        PowerMockito.when(Board.getPawn(new Coordinates(2, 5))).thenReturn(null);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 6))).thenReturn(true);
        PowerMockito.when(Board.getPawn(new Coordinates(3, 6))).thenReturn(enemyPawn);
        PowerMockito.when(Board.isThisSameColor(new Coordinates(3, 6), PawnColor.WHITE)).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(0, 5))).thenReturn(false);
        PowerMockito.when(Board.getPawn(new Coordinates(0, 5))).thenReturn(null);

        pawn.checkPossibleMoves();

        Set<Coordinates> possibleMoves = pawn.getPossibleMoves();

        //Then
        Assert.assertEquals(2, possibleMoves.size());
    }

    @PrepareForTest({Board.class})
    @Test
    public void getPossibleKickIfFieldIsEmpty() {
        //Given
        PowerMockito.mockStatic(Board.class);

        Coordinates coordinates = new Coordinates(1, 7);
        PawnClass pawnClass = new PawnClass(Pawn.KNIGHT, PawnColor.WHITE);
        Knight pawn = new Knight();
        pawn.getPawnCoordinate(coordinates);

        //When
        PowerMockito.when(Board.getPawn(coordinates)).thenReturn(pawnClass);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(2, 5))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 6))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(0, 5))).thenReturn(false);

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

        Coordinates coordinates = new Coordinates(1, 7);
        PawnClass pawnClass = new PawnClass(Pawn.KNIGHT, PawnColor.WHITE);
        PawnClass enemyPawn = new PawnClass(Pawn.QUEEN, PawnColor.BLACK);
        Knight pawn = new Knight();
        pawn.getPawnCoordinate(coordinates);

        //When
        PowerMockito.when(Board.getPawn(coordinates)).thenReturn(pawnClass);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(2, 5))).thenReturn(false);
        PowerMockito.when(Board.getPawn(new Coordinates(2, 5))).thenReturn(null);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 6))).thenReturn(true);
        PowerMockito.when(Board.getPawn(new Coordinates(3, 6))).thenReturn(enemyPawn);
        PowerMockito.when(Board.isThisSameColor(new Coordinates(3, 6), PawnColor.WHITE)).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(0, 5))).thenReturn(true);
        PowerMockito.when(Board.getPawn(new Coordinates(0, 5))).thenReturn(enemyPawn);
        PowerMockito.when(Board.isThisSameColor(new Coordinates(3, 6), PawnColor.WHITE)).thenReturn(false);

        pawn.checkPossibleMoves();

        Set<Coordinates> possibleKick = pawn.getPossibleKick();

        //Then
        Assert.assertEquals(2, possibleKick.size());
    }

    @PrepareForTest({Board.class})
    @Test
    public void getPossibleCheckIfFieldIsEmpty() {
        //Given
        PowerMockito.mockStatic(Board.class);

        Coordinates coordinates = new Coordinates(1, 7);
        PawnClass pawnClass = new PawnClass(Pawn.KNIGHT, PawnColor.WHITE);
        Knight pawn = new Knight();
        pawn.getPawnCoordinate(coordinates);

        //When
        PowerMockito.when(Board.getPawn(coordinates)).thenReturn(pawnClass);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(2, 5))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 6))).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(0, 5))).thenReturn(false);

        pawn.checkPossibleMoves();

        Set<Coordinates> possibleCheck = pawn.getPossibleCheck();

        //Then
        Assert.assertEquals(0, possibleCheck.size());
    }

    @PrepareForTest({Board.class})
    @Test
    public void getPossibleCheckIfFieldIsFull() {
        //Given
        PowerMockito.mockStatic(Board.class);

        Coordinates coordinates = new Coordinates(1, 7);
        PawnClass pawnClass = new PawnClass(Pawn.KNIGHT, PawnColor.WHITE);
        PawnClass enemyPawn = new PawnClass(Pawn.KING, PawnColor.BLACK);
        Knight pawn = new Knight();
        pawn.getPawnCoordinate(coordinates);

        //When
        PowerMockito.when(Board.getPawn(coordinates)).thenReturn(pawnClass);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(2, 5))).thenReturn(false);
        PowerMockito.when(Board.getPawn(new Coordinates(2, 5))).thenReturn(null);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 6))).thenReturn(true);
        PowerMockito.when(Board.getPawn(new Coordinates(3, 6))).thenReturn(enemyPawn);
        PowerMockito.when(Board.isThisSameColor(new Coordinates(3, 6), PawnColor.WHITE)).thenReturn(false);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(0, 5))).thenReturn(false);
        PowerMockito.when(Board.getPawn(new Coordinates(0, 5))).thenReturn(null);

        pawn.checkPossibleMoves();

        Set<Coordinates> possibleCheck = pawn.getPossibleCheck();

        //Then
        Assert.assertEquals(1, possibleCheck.size());
    }

    @PrepareForTest({Board.class})
    @Test
    public void getPossibleCheckIfFieldIsFullAndKingIsThisSameColor() {
        //Given
        PowerMockito.mockStatic(Board.class);

        Coordinates coordinates = new Coordinates(1, 7);
        PawnClass pawnClass = new PawnClass(Pawn.KNIGHT, PawnColor.WHITE);
        PawnClass enemyPawn = new PawnClass(Pawn.KING, PawnColor.WHITE);
        Knight pawn = new Knight();
        pawn.getPawnCoordinate(coordinates);

        //When
        PowerMockito.when(Board.getPawn(coordinates)).thenReturn(pawnClass);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(2, 5))).thenReturn(false);
        PowerMockito.when(Board.getPawn(new Coordinates(2, 5))).thenReturn(null);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(3, 6))).thenReturn(true);
        PowerMockito.when(Board.getPawn(new Coordinates(3, 6))).thenReturn(enemyPawn);
        PowerMockito.when(Board.isThisSameColor(new Coordinates(3, 6), PawnColor.WHITE)).thenReturn(true);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(0, 5))).thenReturn(false);
        PowerMockito.when(Board.getPawn(new Coordinates(0, 5))).thenReturn(null);

        pawn.checkPossibleMoves();

        Set<Coordinates> possibleCheck = pawn.getPossibleCheck();

        //Then
        Assert.assertEquals(0, possibleCheck.size());
    }
}