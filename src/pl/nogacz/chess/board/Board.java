package pl.nogacz.chess.board;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import pl.nogacz.chess.application.Computer;
import pl.nogacz.chess.application.Design;
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

    private Computer computer = new Computer();
    private boolean isComputerRound = false;

    private PawnPromote pawnPromote = new PawnPromote();
    private static Set<Coordinates> possibleMovePromote = new HashSet<>();
    private static Set<Coordinates> possibleKickPromote = new HashSet<>();

    private boolean isSelected = false;
    private Coordinates selectedCoordinates;
    private Set<Coordinates> possibleMoves;
    private Set<Coordinates> possibleKick;

    public Board() {
        addStartPawn();
    }

    public static HashMap<Coordinates, PawnClass> getBoard() {
        return board;
    }

    public static Set<Coordinates> getPossibleMovePromote() {
        return possibleMovePromote;
    }

    public static void addPossibleMovePromote(Set<Coordinates> coordinates) {
        possibleMovePromote.addAll(coordinates);
    }

    public static Set<Coordinates> getPossibleKickPromote() {
        return possibleKickPromote;
    }

    public static void addPossibleKickPromote(Set<Coordinates> coordinates) {
        possibleKickPromote.addAll(coordinates);
    }

    public void addStartPawn() {
        board.put(new Coordinates(0,0), new PawnClass(Pawn.Rook, PawnColor.black));
        board.put(new Coordinates(1,0), new PawnClass(Pawn.Knight, PawnColor.black));
        board.put(new Coordinates(2,0), new PawnClass(Pawn.Bishop, PawnColor.black));
        board.put(new Coordinates(3,0), new PawnClass(Pawn.King, PawnColor.black));
        board.put(new Coordinates(4,0), new PawnClass(Pawn.Queen, PawnColor.black));
        board.put(new Coordinates(5,0), new PawnClass(Pawn.Bishop, PawnColor.black));
        board.put(new Coordinates(6,0), new PawnClass(Pawn.Knight, PawnColor.black));
        board.put(new Coordinates(7,0), new PawnClass(Pawn.Rook, PawnColor.black));

        for(int i = 0; i < 8; i++) {
            board.put(new Coordinates(i, 1), new PawnClass(Pawn.Pawn, PawnColor.black));
            board.put(new Coordinates(i, 6), new PawnClass(Pawn.Pawn, PawnColor.white));
        }

        board.put(new Coordinates(0,7), new PawnClass(Pawn.Rook, PawnColor.white));
        board.put(new Coordinates(1,7), new PawnClass(Pawn.Knight, PawnColor.white));
        board.put(new Coordinates(2,7), new PawnClass(Pawn.Bishop, PawnColor.white));
        board.put(new Coordinates(3,7), new PawnClass(Pawn.Queen, PawnColor.white));
        board.put(new Coordinates(4,7), new PawnClass(Pawn.King, PawnColor.white));
        board.put(new Coordinates(5,7), new PawnClass(Pawn.Bishop, PawnColor.white));
        board.put(new Coordinates(6,7), new PawnClass(Pawn.Knight, PawnColor.white));
        board.put(new Coordinates(7,7), new PawnClass(Pawn.Rook, PawnColor.white));

        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            Design.addPawn(entry.getKey(), entry.getValue());
        }
    }

    public void readMouseEvent(MouseEvent event) {
        Coordinates eventCoordinates = new Coordinates((int) ((event.getX() - 39) / 84), (int) ((event.getY() - 39) / 85));

        if(eventCoordinates.getX() <= 7 && eventCoordinates.getY() <= 7 && !isComputerRound) {
            if(isSelected) {
                if(eventCoordinates.getX() == selectedCoordinates.getX() && eventCoordinates.getY() == selectedCoordinates.getY()) {
                    selectedCoordinates = null;
                    isSelected = false;
                    unLightSelect(eventCoordinates);
                } else if(isPossibleMove(eventCoordinates)) {
                    unLightSelect(selectedCoordinates);
                    movePawn(selectedCoordinates, eventCoordinates);

                    selectedCoordinates = null;
                    isSelected = false;

                    checkPromote(eventCoordinates, 1);

                    computerMove();
                }
            } else {
                if(isFieldNotNull(eventCoordinates)) {
                    if(getPawn(eventCoordinates).getColor().equals(PawnColor.white)) {
                        possibleMovePromote.clear();
                        possibleKickPromote.clear();

                        selectedCoordinates = eventCoordinates;
                        isSelected = true;
                        lightSelect(eventCoordinates);
                    }
                }
            }
        }
    }

    public void computerMove() {
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
                Coordinates moveCoordinates = computer.chooseMove(selectedCoordinates);
                unLightSelect(selectedCoordinates);
                movePawn(selectedCoordinates, moveCoordinates);

                isComputerRound = false;
                selectedCoordinates = null;

                checkPromote(moveCoordinates, 1);
            }
        });

        isComputerRound = true;
        computer.getGameData();
        selectedCoordinates = computer.choosePawn();

        lightSelect(selectedCoordinates);

        new Thread(computerSleep).start();
    }

    public void checkPromote(Coordinates coordinates, int type) {
        if(type == 0) {
            if(possibleMovePromote.contains(coordinates)) {
                pawnPromote.userPromote(coordinates);
            } else if(possibleKickPromote.contains(coordinates)) {
                pawnPromote.userPromote(coordinates);
            }
        } else {
            if(possibleMovePromote.contains(coordinates)) {
                pawnPromote.computerPromote(coordinates);
            } else if(possibleKickPromote.contains(coordinates)) {
                pawnPromote.computerPromote(coordinates);
            }
        }
    }

    public boolean isPossibleMove(Coordinates coordinates) {
        if(possibleMoves.contains(coordinates) || possibleKick.contains(coordinates)) {
            return true;
        }

        return false;
    }

    public static boolean isFieldNotNull(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);

        if(pawn != null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isThisSameColor(Coordinates coordinates, PawnColor color) {
        PawnClass pawn = getPawn(coordinates);
        if(pawn.getColor().equals(color)) {
            return true;
        } else {
            return false;
        }
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

    public void movePawn(Coordinates oldCoordinates, Coordinates newCoordinates) {
        PawnClass pawn = getPawn(oldCoordinates);
        Design.removePawn(oldCoordinates);
        Design.removePawn(newCoordinates);
        Design.addPawn(newCoordinates, pawn);

        board.remove(oldCoordinates);
        board.put(newCoordinates, pawn);
    }

    public void lightSelect(Coordinates coordinates) {
        PawnMoves pawnMoves = new PawnMoves(getPawn(coordinates), coordinates);

        possibleMoves = pawnMoves.getPossibleMoves();
        possibleKick = pawnMoves.getPossibleKick();

        possibleMoves.forEach(this::lightMove);
        possibleKick.forEach(this::lightPawn);

        lightPawn(coordinates);
    }

    public void lightPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        Design.removePawn(coordinates);
        Design.addLightPawn(coordinates, pawn);
    }

    public void lightMove(Coordinates coordinates) {
        Design.addLightMove(coordinates);
    }

    public void unLightSelect(Coordinates coordinates) {
        possibleMoves.forEach(this::unLightMove);
        possibleKick.forEach(this::unLightPawn);

        unLightPawn(coordinates);
    }

    public void unLightPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        Design.removePawn(coordinates);
        Design.addPawn(coordinates, pawn);
    }

    public void unLightMove(Coordinates coordinates) {
        Design.removePawn(coordinates);
    }
}
