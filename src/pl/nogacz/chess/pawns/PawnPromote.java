package pl.nogacz.chess.pawns;

import javafx.scene.control.ChoiceDialog;
import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * @author Dawid Nogacz on 02.05.2019
 */
public class PawnPromote {
    private Random random = new Random();

    private Pawn[] pawns = {Pawn.Queen, Pawn.Knight, Pawn.Rook, Pawn.Bishop};
    private List<Pawn> dialogData = Arrays.asList(pawns);

    public void userPromote(Coordinates coordinates) {
        ChoiceDialog dialog = new ChoiceDialog(dialogData.get(0), dialogData);
        dialog.setTitle("Pawn upgrade");
        dialog.setHeaderText("Select your choice");

        Optional<Pawn> result = dialog.showAndWait();

        if (result.isPresent()) {
            Board.promotePawn(coordinates, result.get());
        }
    }

    public void computerPromote(Coordinates coordinates) {
        Object[] object = dialogData.toArray();
        Pawn pawn = (Pawn) object[random.nextInt(object.length)];

        Board.promotePawn(coordinates, pawn);
    }
}
