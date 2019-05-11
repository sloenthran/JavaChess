package pl.nogacz.chess.application;

import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.PawnClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dawid Nogacz on 11.05.2019
 */
public class ChessNotation implements Serializable {
    private static List<String> movesList = new ArrayList<>();

    private String playerMove = null;
    private String computerMove = null;

    public static List<String> getMovesList() {
        return movesList;
    }

    public static void setMovesList(List<String> movesList) {
        ChessNotation.movesList = movesList;
        updateTextArea();
    }

    public void addMovement(Coordinates oldCoordinates, Coordinates newCoordinates, PawnClass pawn) {
        if(pawn.getColor().isWhite()) {
            playerMove = getPawnInNotation(pawn) + getCoordinatesInNotation(oldCoordinates) + getCoordinatesInNotation(newCoordinates);
        } else {
            computerMove = getPawnInNotation(pawn) + getCoordinatesInNotation(oldCoordinates) + getCoordinatesInNotation(newCoordinates);
        }
    }

    public void saveRound() {
        movesList.add(playerMove + " " + computerMove);

        playerMove = null;
        computerMove = null;

        updateTextArea();
    }

    private static void updateTextArea() {
        int round = 0;
        String text = "";

        for(String move : movesList) {
            round++;
            text = text + round + ". " + move + "\n";
        }

        Design.setTextInTextArea(text);
    }

    private String getPawnInNotation(PawnClass pawn) {
        switch(pawn.getPawn()) {
            case BISHOP: return "G";
            case KING: return "K";
            case KNIGHT: return "S";
            case QUEEN: return "H";
            case ROOK: return "W";
            default: return "";
        }
    }

    private String getCoordinatesInNotation(Coordinates coordinates) {
        return getXInNotation(coordinates.getX()) + getYInNotation(coordinates.getY());
    }

    private String getXInNotation(int x) {
        switch (x) {
            case 0: return "a";
            case 1: return "b";
            case 2: return "c";
            case 3: return "d";
            case 4: return "e";
            case 5: return "f";
            case 6: return "g";
            default: return "h";
        }
    }

    private String getYInNotation(int y) {
        switch (y) {
            case 0: return "8";
            case 1: return "7";
            case 2: return "6";
            case 3: return "5";
            case 4: return "4";
            case 5: return "3";
            case 6: return "2";
            default: return "1";
        }
    }
}
