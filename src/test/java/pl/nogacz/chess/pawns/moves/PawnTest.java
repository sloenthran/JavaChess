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
public class PawnTest {
    @PrepareForTest({Board.class})
    @Test
    public void getPossibleMovesIfFieldIsEmpty() {
        //Given
        PowerMockito.mockStatic(Board.class);

        Coordinates coordinates = new Coordinates(0, 6);
        PawnClass pawnClass = new PawnClass(Pawn.Pawn, PawnColor.white);
        pl.nogacz.chess.pawns.moves.Pawn pawn = new pl.nogacz.chess.pawns.moves.Pawn();
        pawn.getPawnCoordinate(coordinates);

        //When
        PowerMockito.when(Board.getPawn(coordinates)).thenReturn(pawnClass);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(coordinates.getX(), coordinates.getY() - 1))).thenReturn(false);

        pawn.checkPossibleMoves();

        Set<Coordinates> possibleMoves = pawn.getPossibleMoves();

        //Then
        Assert.assertEquals(1, possibleMoves.size());
    }

    @PrepareForTest({Board.class})
    @Test
    public void getPossibleMovesIfFieldIsFull() {
        //Given
        PowerMockito.mockStatic(Board.class);

        Coordinates coordinates = new Coordinates(0, 6);
        PawnClass pawnClass = new PawnClass(Pawn.Pawn, PawnColor.white);
        pl.nogacz.chess.pawns.moves.Pawn pawn = new pl.nogacz.chess.pawns.moves.Pawn();
        pawn.getPawnCoordinate(coordinates);

        //When
        PowerMockito.when(Board.getPawn(coordinates)).thenReturn(pawnClass);
        PowerMockito.when(Board.isFieldNotNull(new Coordinates(coordinates.getX(), coordinates.getY() - 1))).thenReturn(true);

        pawn.checkPossibleMoves();

        Set<Coordinates> possibleMoves = pawn.getPossibleMoves();

        //Then
        Assert.assertEquals(0, possibleMoves.size());
    }

    @PrepareForTest({Board.class})
    @Test
    public void getPossibleKickIfFieldIsEmpty() {
        //Given
        PowerMockito.mockStatic(Board.class);

        Coordinates coordinates = new Coordinates(1, 6);
        PawnClass pawnClass = new PawnClass(Pawn.Pawn, PawnColor.white);
        pl.nogacz.chess.pawns.moves.Pawn pawn = new pl.nogacz.chess.pawns.moves.Pawn();
        pawn.getPawnCoordinate(coordinates);

        //When
        PowerMockito.when(Board.getPawn(coordinates)).thenReturn(pawnClass);

        PowerMockito.when(Board.isFieldNotNull(new Coordinates(coordinates.getX() + 1, coordinates.getY() - 1))).thenReturn(false);
        PowerMockito.when(Board.getPawn(new Coordinates(coordinates.getX() + 1, coordinates.getY() - 1))).thenReturn(null);
        PowerMockito.when(Board.isThisSameColor(new Coordinates(coordinates.getX() + 1, coordinates.getY() - 1), PawnColor.white)).thenReturn(false);

        PowerMockito.when(Board.isFieldNotNull(new Coordinates(coordinates.getX() - 1, coordinates.getY() - 1))).thenReturn(false);
        PowerMockito.when(Board.getPawn(new Coordinates(coordinates.getX() - 1, coordinates.getY() - 1))).thenReturn(null);
        PowerMockito.when(Board.isThisSameColor(new Coordinates(coordinates.getX() + 1, coordinates.getY() - 1), PawnColor.white)).thenReturn(false);

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

        Coordinates coordinates = new Coordinates(1, 6);
        PawnClass pawnClass = new PawnClass(Pawn.Pawn, PawnColor.white);
        PawnClass enemyPawnClass = new PawnClass(Pawn.Pawn, PawnColor.black);
        pl.nogacz.chess.pawns.moves.Pawn pawn = new pl.nogacz.chess.pawns.moves.Pawn();
        pawn.getPawnCoordinate(coordinates);

        //When
        PowerMockito.when(Board.getPawn(coordinates)).thenReturn(pawnClass);

        PowerMockito.when(Board.isFieldNotNull(new Coordinates(coordinates.getX() + 1, coordinates.getY() - 1))).thenReturn(true);
        PowerMockito.when(Board.getPawn(new Coordinates(coordinates.getX() + 1, coordinates.getY() - 1))).thenReturn(enemyPawnClass);
        PowerMockito.when(Board.isThisSameColor(new Coordinates(coordinates.getX() + 1, coordinates.getY() - 1), PawnColor.white)).thenReturn(false);

        PowerMockito.when(Board.isFieldNotNull(new Coordinates(coordinates.getX() - 1, coordinates.getY() - 1))).thenReturn(true);
        PowerMockito.when(Board.isThisSameColor(new Coordinates(coordinates.getX() - 1, coordinates.getY() - 1), PawnColor.white)).thenReturn(true);

        pawn.checkPossibleMoves();

        Set<Coordinates> possibleKick = pawn.getPossibleKick();

        //Then
        Assert.assertEquals(1, possibleKick.size());
    }

    //@Test
    public void getPossibleCheck() {
        //TODO
    }
}