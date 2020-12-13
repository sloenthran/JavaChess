package pl.nogacz.chess.application.menu;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import jdk.internal.net.http.common.SubscriberWrapper;
import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.application.ChessNotation;
import pl.nogacz.chess.pawns.PawnClass;

public class Undo{

    public Undo(){
        Coordinates oldCoordinate;
        Coordinates newCoordinate;
        boolean playerKicked=false;
        boolean computerKicked=false;

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("JavaChess");
        alert.setContentText("Undo last move");

        String lastMove=ChessNotation.removeMovement();

        String computer=lastMove.substring(lastMove.indexOf(" ")+1);
        if((computer.charAt(0)=='H') || (computer.charAt(0)=='S') || (computer.charAt(0)=='K') || (computer.charAt(0)=='W') || (computer.charAt(0)=='G'))
            computer=computer.substring(1);
        if(computer.charAt(0)=='x'){
            playerKicked=true;
            computer=computer.substring(1);
        }
        String computerOld=computer.substring(0,2);
        String computerNew=computer.substring(2);
        oldCoordinate=getCoordinate(computerOld);
        newCoordinate=getCoordinate(computerNew);
        Board.undo(oldCoordinate, newCoordinate);
        if(playerKicked){
            PawnClass pawn=ChessNotation.removeKicked();
            Board.resurrection(pawn, newCoordinate);
        }

        String player=lastMove.substring(0,lastMove.indexOf(" "));
        if((player.charAt(0)=='H') || (player.charAt(0)=='S') || (player.charAt(0)=='K') || (player.charAt(0)=='W') || (player.charAt(0)=='G'))
            player=player.substring(1);
        if(player.charAt(0)=='x'){
            computerKicked=true;
            player=player.substring(1);
        }
        String playerOld=player.substring(0,2);
        String playerNew=player.substring(2);
        oldCoordinate=getCoordinate(playerOld);
        newCoordinate=getCoordinate(playerNew);
        Board.undo(oldCoordinate, newCoordinate);
        if(computerKicked){
            PawnClass pawn=ChessNotation.removeKicked();
            Board.resurrection(pawn, newCoordinate);
        }


    }
    public Coordinates getCoordinate(String s){
        char letter=s.charAt(0);
        int y=Integer.parseInt(s.substring(1));
        int x=-1;
        y--;
        if(letter=='a')
            x=0;
        else if(letter=='b')
            x=1;
        else if(letter=='c')
            x=2;
        else if(letter=='d')
            x=3;
        else if(letter=='e')
            x=4;
        else if(letter=='f')
            x=5;
        else if(letter=='g')
            x=6;
        else if(letter=='h')
            x=7;
        y=7-y;
        Coordinates coordinate=new Coordinates(x,y);
        return coordinate;
    }
}