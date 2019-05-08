package pl.nogacz.chess.board;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import pl.nogacz.chess.application.Computer;
import pl.nogacz.chess.application.Design;
import pl.nogacz.chess.application.EndGame;
import pl.nogacz.chess.application.GameLogic;
import pl.nogacz.chess.pawns.Pawn;
import pl.nogacz.chess.pawns.PawnClass;
import pl.nogacz.chess.pawns.PawnColor;
import pl.nogacz.chess.pawns.PawnPromote;
import pl.nogacz.chess.pawns.moves.PawnMoves;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class Board {
    private static HashMap<Coordinates, PawnClass> board = new HashMap<>();
    private GameLogic gameLogic = new GameLogic();

    private Computer computer = new Computer();
    private boolean isComputerRound = false;

    private PawnPromote pawnPromote = new PawnPromote();
    private static Set<Coordinates> possibleMovePromote = new HashSet<>();
    private static Set<Coordinates> possibleKickPromote = new HashSet<>();

    private boolean isSelected = false;
    private Coordinates selectedCoordinates;
    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possibleCheck = new HashSet<>();

    private boolean isKingChecked = false;
    private Set<Coordinates> possiblePawnIfKingIsChecked = new HashSet<>();

    public Board() {
        addStartPawn();
    }

    public static HashMap<Coordinates, PawnClass> getBoard() {
        return board;
    }

    public static void addPossibleMovePromote(Set<Coordinates> coordinates) {
        possibleMovePromote.addAll(coordinates);
    }

    public static void addPossibleKickPromote(Set<Coordinates> coordinates) {
        possibleKickPromote.addAll(coordinates);
    }

    private void addStartPawn() {
        board.put(new Coordinates(0,0), new PawnClass(Pawn.ROOK, PawnColor.BLACK));
        board.put(new Coordinates(1,0), new PawnClass(Pawn.KNIGHT, PawnColor.BLACK));
        board.put(new Coordinates(2,0), new PawnClass(Pawn.BISHOP, PawnColor.BLACK));
        board.put(new Coordinates(3,0), new PawnClass(Pawn.KING, PawnColor.BLACK));
        board.put(new Coordinates(4,0), new PawnClass(Pawn.QUEEN, PawnColor.BLACK));
        board.put(new Coordinates(5,0), new PawnClass(Pawn.BISHOP, PawnColor.BLACK));
        board.put(new Coordinates(6,0), new PawnClass(Pawn.KNIGHT, PawnColor.BLACK));
        board.put(new Coordinates(7,0), new PawnClass(Pawn.ROOK, PawnColor.BLACK));

        for(int i = 0; i < 8; i++) {
            board.put(new Coordinates(i, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
            board.put(new Coordinates(i, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        }

        board.put(new Coordinates(0,7), new PawnClass(Pawn.ROOK, PawnColor.WHITE));
        board.put(new Coordinates(1,7), new PawnClass(Pawn.KNIGHT, PawnColor.WHITE));
        board.put(new Coordinates(2,7), new PawnClass(Pawn.BISHOP, PawnColor.WHITE));
        board.put(new Coordinates(3,7), new PawnClass(Pawn.QUEEN, PawnColor.WHITE));
        board.put(new Coordinates(4,7), new PawnClass(Pawn.KING, PawnColor.WHITE));
        board.put(new Coordinates(5,7), new PawnClass(Pawn.BISHOP, PawnColor.WHITE));
        board.put(new Coordinates(6,7), new PawnClass(Pawn.KNIGHT, PawnColor.WHITE));
        board.put(new Coordinates(7,7), new PawnClass(Pawn.ROOK, PawnColor.WHITE));

        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            Design.addPawn(entry.getKey(), entry.getValue());
        }
    }

    public void readMouseEvent(MouseEvent event) {
        if(isComputerRound) {
            return;
        }

        Coordinates eventCoordinates = new Coordinates((int) ((event.getX() - 39) / 84), (int) ((event.getY() - 39) / 85));
        gameLogic.prepareData();

        if(!gameLogic.isMovePossible()) {
            endGame("Draw. None of the players has any more moves ... :(");
        }  else if(isKingChecked && possiblePawnIfKingIsChecked.size() == 0) {
            endGame("You loss. Maybe you try again?");
        } else if(eventCoordinates.isValid()) {
            if(isSelected) {
                if(eventCoordinates.equals(selectedCoordinates)) {
                    selectedCoordinates = null;
                    isSelected = false;

                    unLightSelect(eventCoordinates);

                    if(isKingChecked) {
                        possiblePawnIfKingIsChecked.forEach(this::lightPawn);
                    }
                } else if(isPossibleMove(eventCoordinates)) {
                    unLightSelect(selectedCoordinates);
                    movePawn(selectedCoordinates, eventCoordinates);

                    selectedCoordinates = null;
                    isSelected = false;
                    isKingChecked = false;

                    checkPromote(eventCoordinates, 0);

                    computerMove();
                }
            } else {
                if(isFieldNotNull(eventCoordinates)) {
                    if(Board.getPawn(eventCoordinates).getColor().isBlack()) {
                        return;
                    }

                    possibleMovePromote.clear();
                    possibleKickPromote.clear();

                    if(isPossibleSelect(eventCoordinates, PawnColor.WHITE)) {
                        selectedCoordinates = eventCoordinates;
                        isSelected = true;
                        lightSelect(eventCoordinates);
                    }
                }
            }
        }
    }

    private void computerMove() {
        Task<Void> computerSleep = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                return null;
            }
        };

        computerSleep.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if(isKingChecked) {
                    if(possibleKick.size() > 0) {
                        Coordinates moveCoordinates = computer.selectRandom(possibleKick);
                        unLightSelect(selectedCoordinates);
                        movePawn(selectedCoordinates, moveCoordinates);

                        checkPromote(moveCoordinates, 1);

                        isKingChecked = false;
                        isComputerRound = false;
                        selectedCoordinates = null;
                    } else {
                        endGame("You win! Congratulations :)");
                    }
                } else {
                    Coordinates moveCoordinates = computer.chooseMove(selectedCoordinates);
                    unLightSelect(selectedCoordinates);
                    movePawn(selectedCoordinates, moveCoordinates);

                    checkPromote(moveCoordinates, 1);

                    isComputerRound = false;
                    selectedCoordinates = null;
                }
            }
        });

        isComputerRound = true;
        computer.getGameData();
        gameLogic.prepareData();

        if(gameLogic.isKingChecked(PawnColor.BLACK)) {
            possiblePawnIfKingIsChecked = gameLogic.getPossiblePawnIfKingIsChecked(PawnColor.BLACK);

            if(possiblePawnIfKingIsChecked.size() == 0) {
                endGame("You win! Congratulations! :)");
            } else {
                selectedCoordinates = computer.selectRandom(possiblePawnIfKingIsChecked);
                lightSelect(selectedCoordinates);

                new Thread(computerSleep).start();
            }
        } else if(gameLogic.isMovePossible()) {
            selectedCoordinates = computer.choosePawn();

            lightSelect(selectedCoordinates);

            new Thread(computerSleep).start();
        } else {
            endGame("Draw. None of the players has any more moves ... :(");
        }
    }

    private void endGame(String message) {
        new EndGame(message);
    }

    private void checkPromote(Coordinates coordinates, int type) {
        PawnClass pawn = getPawn(coordinates);

        if(type == 0 && pawn.getColor().isWhite()) {
            if(possibleMovePromote.contains(coordinates)) {
                pawnPromote.userPromote(coordinates);
            } else if(possibleKickPromote.contains(coordinates)) {
                pawnPromote.userPromote(coordinates);
            }
        } else if(pawn.getColor().isBlack()) {
            if(possibleMovePromote.contains(coordinates)) {
                pawnPromote.computerPromote(coordinates);
            } else if(possibleKickPromote.contains(coordinates)) {
                pawnPromote.computerPromote(coordinates);
            }
        }
    }

    private boolean isPossibleSelect(Coordinates coordinates, PawnColor color) {
        isKingChecked = gameLogic.isKingChecked(color);

        if(isKingChecked) {
            possiblePawnIfKingIsChecked = gameLogic.getPossiblePawnIfKingIsChecked(color);

            possiblePawnIfKingIsChecked.forEach(this::lightPawn);

            System.out.println(possiblePawnIfKingIsChecked.contains(coordinates));

            return possiblePawnIfKingIsChecked.contains(coordinates);
        }

        return true;
    }

    private boolean isPossibleMove(Coordinates coordinates) {
        return possibleMoves.contains(coordinates) || possibleKick.contains(coordinates);
    }

    public static boolean isFieldNotNull(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        return pawn != null;
    }

    public static boolean isThisSameColor(Coordinates coordinates, PawnColor color) {
        PawnClass pawn = getPawn(coordinates);
        return pawn.getColor() == color;
    }

    public static PawnClass getPawn(Coordinates coordinates) {
        PawnClass pawn = board.get(coordinates);
        return pawn;
    }

    public static PawnClass addPawnWithoutDesign(Coordinates coordinates, PawnClass pawn) {
        PawnClass oldPawn = null;

        if(isFieldNotNull(coordinates)) {
            oldPawn = getPawn(coordinates);
            board.remove(coordinates);
        }

        board.put(coordinates, pawn);
        return oldPawn;
    }

    public static void promotePawn(Coordinates coordinates, Pawn pawn) {
        PawnClass oldPawn = getPawn(coordinates);
        PawnClass newPawn = new PawnClass(pawn, oldPawn.getColor());

        board.remove(coordinates);
        board.put(coordinates, newPawn);

        Design.removePawn(coordinates);
        Design.addPawn(coordinates, newPawn);
    }

    public static void removePawnWithoutDesign(Coordinates coordinates) {
        board.remove(coordinates);
    }

    private void movePawn(Coordinates oldCoordinates, Coordinates newCoordinates) {
        PawnClass pawn = getPawn(oldCoordinates);
        Design.removePawn(oldCoordinates);
        Design.removePawn(newCoordinates);
        Design.addPawn(newCoordinates, pawn);

        board.remove(oldCoordinates);
        board.put(newCoordinates, pawn);
    }

    private void lightSelect(Coordinates coordinates) {
        PawnMoves pawnMoves = new PawnMoves(getPawn(coordinates), coordinates);

        if(isKingChecked) {
            possibleKick = gameLogic.getPossibleKickIfKingIsChecked(coordinates);

            if(getPawn(coordinates).getPawn().isKing()) {
                possibleMoves = gameLogic.getPossibleMovesIfKingIsChecked(coordinates);
            }

            possiblePawnIfKingIsChecked.forEach(this::unLightPawn);
        } else {
            possibleMoves = pawnMoves.getPossibleMoves();
            possibleKick = pawnMoves.getPossibleKick();
            possibleCheck = pawnMoves.getPossibleCheck();
        }

        possibleMoves.forEach(this::lightMove);
        possibleKick.forEach(this::lightPawn);
        possibleCheck.forEach(this::checkedPawn);

        lightPawn(coordinates);
    }

    private void lightPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        Design.removePawn(coordinates);
        Design.addLightPawn(coordinates, pawn);
    }

    private void checkedPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        if(pawn != null) {
            Design.removePawn(coordinates);
            Design.addCheckedPawn(coordinates, pawn);
        }
    }

    private void lightMove(Coordinates coordinates) {
        Design.addLightMove(coordinates);
    }

    private void unLightSelect(Coordinates coordinates) {
        possibleMoves.forEach(this::unLightMove);
        possibleKick.forEach(this::unLightPawn);

        possibleCheck.forEach(this::unLightPawn);

        unLightPawn(coordinates);
    }

    private void unLightPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);

        if(pawn != null) {
            Design.removePawn(coordinates);
            Design.addPawn(coordinates, pawn);
        }
    }

    private void unLightMove(Coordinates coordinates) {
        Design.removePawn(coordinates);
    }
}
