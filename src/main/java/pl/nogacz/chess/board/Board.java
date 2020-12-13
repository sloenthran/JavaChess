package pl.nogacz.chess.board;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import pl.nogacz.chess.application.*;
import pl.nogacz.chess.application.menu.EndGame;
import pl.nogacz.chess.application.menu.Statistics;
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
    private SaveGame saveGame = new SaveGame();
    private ChessNotation chessNotation = new ChessNotation();
    private Statistics statistics = new Statistics();

    private static HashMap<Coordinates, PawnClass> board = new HashMap<>();
    private GameLogic gameLogic = new GameLogic();
    private boolean isGameEnd = false;

    private static Computer computer = new Computer();
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
        if(saveGame.isSave()) {
            saveGame.load();

            for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
                Design.addPawn(entry.getKey(), entry.getValue());
            }
        } else {
            addStartPawn();
        }
    }

    public static HashMap<Coordinates, PawnClass> getBoard() {
        return board;
    }

    public static void setBoard(HashMap<Coordinates, PawnClass> board) {
        Board.board = board;
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
        if(isComputerRound || isGameEnd) {
            return;
        }

        Coordinates eventCoordinates = new Coordinates((int) ((event.getX() - 37) / 84), (int) ((event.getY() - 37) / 84));

        if(!eventCoordinates.isValid()) {
            return;
        }

        gameLogic.prepareData();

        if(!gameLogic.isMovePossible()) {
            noMovePossibleInfo();
        }  else if(isKingChecked && possiblePawnIfKingIsChecked.size() == 0) {
            if(gameLogic.isKingChecked(PawnColor.BLACK)) {
                statistics.addGameWin();
                endGame("You win! Congratulations :)");
            } else {
                statistics.addGameLoss();
                endGame("You loss. Maybe you try again?");
            }
        } else {
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

                    chessNotation.addMovement(selectedCoordinates, eventCoordinates, getPawn(eventCoordinates), possibleKick.contains(eventCoordinates));

                    selectedCoordinates = null;
                    isSelected = false;
                    isKingChecked = false;

                    checkPromote(eventCoordinates, 0);

                    computerMove();
                } else if(isFieldNotNull(eventCoordinates) && Board.getPawn(eventCoordinates).getColor().isWhite() && isPossibleSelect(eventCoordinates, PawnColor.WHITE)) {
                    possibleMovePromote.clear();
                    possibleKickPromote.clear();

                    unLightSelect(selectedCoordinates);

                    selectedCoordinates = eventCoordinates;
                    isSelected = true;
                    lightSelect(eventCoordinates);
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

    public void readKeyboard(KeyEvent event) {
        if(event.getCode().equals(KeyCode.R) || event.getCode().equals(KeyCode.N)) {
            new EndGame("").newGame();
        }
    }

    public static void setComputerSkill(int skill) {
        computer.setSkill(skill);
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

                        chessNotation.addMovement(selectedCoordinates, moveCoordinates, getPawn(moveCoordinates), possibleKick.contains(moveCoordinates));

                        checkPromote(moveCoordinates, 1);
                    } else if(possibleMoves.size() > 0) {
                        Coordinates moveCoordinates = computer.selectRandom(possibleMoves);
                        unLightSelect(selectedCoordinates);
                        movePawn(selectedCoordinates, moveCoordinates);

                        chessNotation.addMovement(selectedCoordinates, moveCoordinates, getPawn(moveCoordinates), false);

                        checkPromote(moveCoordinates, 1);
                    } else {
                        statistics.addGameWin();
                        endGame("You win! Congratulations :)");
                    }

                    isKingChecked = false;
                } else {
                    Coordinates moveCoordinates = computer.chooseMove(selectedCoordinates);
                    unLightSelect(selectedCoordinates);
                    movePawn(selectedCoordinates, moveCoordinates);

                    chessNotation.addMovement(selectedCoordinates, moveCoordinates, getPawn(moveCoordinates), possibleKick.contains(moveCoordinates));

                    checkPromote(moveCoordinates, 1);
                }

                isComputerRound = false;
                selectedCoordinates = null;
                chessNotation.saveRound();
                saveGame.save();
            }
        });

        isComputerRound = true;
        computer.getGameData();
        gameLogic.prepareData();

        if(gameLogic.isKingChecked(PawnColor.BLACK)) {
            possiblePawnIfKingIsChecked = gameLogic.getPossiblePawnIfKingIsChecked(PawnColor.BLACK);

            if(possiblePawnIfKingIsChecked.size() == 0) {
                statistics.addGameWin();
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
            noMovePossibleInfo();
        }
    }

    private void endGame(String message) {
        isGameEnd = true;
        new EndGame(message).printDialog();
    }

    private void noMovePossibleInfo() {
        switch(gameLogic.getWinner()) {
            case DRAW_COLOR: { statistics.addGameDraw(); endGame("Draw. Maybe you try again?"); break; }
            case WHITE: { statistics.addGameWin(); endGame("You win! Congratulations! :)"); break; }
            case BLACK: { statistics.addGameLoss(); endGame("You loss. Maybe you try again?"); break; }
        }
    }

    private void checkPromote(Coordinates coordinates, int type) {
        PawnClass pawn = getPawn(coordinates);

        if(pawn.getPawn() != Pawn.PAWN) return;

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
        PawnClass kicked=getPawn(newCoordinates);
        if(kicked!=null)
            ChessNotation.addKicked(kicked);
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

    public static void undo(Coordinates oldCoordinates, Coordinates newCoordinates) {
        PawnClass pawn = getPawn(newCoordinates);//oldCoordinates reverse|new Coordinate null x and y is false?
        Design.removePawn(newCoordinates);
        Design.addPawn(oldCoordinates, pawn);

        board.remove(newCoordinates);
        board.put(oldCoordinates, pawn);
    }
    
    public static void resurrection(PawnClass pawn, Coordinates coordinate){
        Design.addPawn(coordinate, pawn);
        board.put(coordinate, pawn);
    }
}
