package pl.nogacz.chess.pawns;

import javafx.scene.control.ChoiceDialog;
import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Dawid Nogacz on 02.05.2019
 */
public class PawnPromote {
    public void userPromote(Coordinates coordinates) {
        Pawn[] pawns = {Pawn.Queen, Pawn.Knight, Pawn.Rook, Pawn.Bishop};
        List<Pawn> dialogData = Arrays.asList(pawns);

        ChoiceDialog dialog = new ChoiceDialog(dialogData.get(0), dialogData);
        dialog.setTitle("Pawn upgrade");
        dialog.setHeaderText("Select your choice");

        Optional<Pawn> result = dialog.showAndWait();

        if (result.isPresent()) {
            Board.promotePawn(coordinates, result.get());
        }
    }
}
