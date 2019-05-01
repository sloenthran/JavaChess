package pl.nogacz.chess.pawns;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Dawid Nogacz on 01.05.2019
 */
public class PawnClass {
    private Pawn pawn;
    private PawnColor color;

    public PawnClass(Pawn pawn, PawnColor color) {
        this.pawn = pawn;
        this.color = color;
    }

    public Pawn getPawn() {
        return pawn;
    }

    public PawnColor getColor() {
        return color;
    }

    public ImageView getImage() {
        Image image = new Image("file:resources/pawns/"+ color +"_"+ pawn +".png");
        return new ImageView(image);
    }

    public ImageView getLightImage() {
        Image image = new Image("file:resources/pawns/light_"+ color +"_"+ pawn +".png");
        return new ImageView(image);
    }
}