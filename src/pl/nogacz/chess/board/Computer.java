package pl.nogacz.chess.board;

import pl.nogacz.chess.pawns.PawnClass;
import pl.nogacz.chess.pawns.PawnColor;
import pl.nogacz.chess.pawns.moves.PawnMoves;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class Computer {
    private HashMap<Coordinates, PawnClass> cacheBoard = new HashMap<>();
    private Random random = new Random();

    public void getGameData() {
        cacheBoard.clear();

        for(Map.Entry<Coordinates, PawnClass> entry : Board.getBoard().entrySet()) {
            if(entry.getValue().getColor().equals(PawnColor.black)) {
                PawnMoves moves = new PawnMoves(entry.getValue(), entry.getKey());
                if(moves.getPossibleMoves().size() > 0 || moves.getPossibleKick().size() > 0) {
                    cacheBoard.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public void choosePawn() {
        Object[] object = cacheBoard.values().toArray();
        Object randomValue = object[random.nextInt(object.length)];
        System.out.println(randomValue);
    }
}
