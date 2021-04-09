package pl.nogacz.chess.application;

import com.google.gson.Gson;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pl.nogacz.chess.application.menu.Statistics;
import pl.nogacz.chess.board.Board;
import pl.nogacz.chess.board.Coordinates;
import pl.nogacz.chess.pawns.Pawn;
import pl.nogacz.chess.pawns.PawnClass;
import pl.nogacz.chess.pawns.PawnColor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ContributedLoadGame {

    public void load(){
        DirectoryChooser dirChooser = new DirectoryChooser();

        dirChooser.setTitle("Choose location");

        File selectedDir = dirChooser.showDialog(new Stage());
        if(selectedDir != null) {
            loadBoard(selectedDir);
            loadChessNotation(selectedDir);
            loadComputer(selectedDir);
            loadStatistics(selectedDir);
        }
    }

    private void loadBoard(File dir){

        String filePath = dir.getAbsolutePath();
        filePath = filePath + "/board.txt";
        Scanner sc = null;
        try {
            sc = new Scanner(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<Coordinates, PawnClass> cacheMap = new HashMap<>();
        while(sc.hasNext()){
            String line = sc.next();
            String [] tokens = line.split(",");
            int Xcoordinate = Integer.parseInt(tokens[0]);
            int Ycoordinate = Integer.parseInt(tokens[1]);
            String color = tokens[2];
            String pawnstr = tokens[3];
            switch (pawnstr){
                case ("PAWN"): {
                    if (color.equals("BLACK"))
                        cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
                    else
                        cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
                    break;
                } case ("KNIGHT"): {
                    if (color.equals("BLACK"))
                        cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.KNIGHT, PawnColor.BLACK));
                    else
                        cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.KNIGHT, PawnColor.WHITE));
                    break;
                } case ("KING"): {
                    if (color.equals("BLACK"))
                        cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.KING, PawnColor.BLACK));
                    else
                        cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.KING, PawnColor.WHITE));
                    break;
                } case ("BISHOP"): {
                    if (color.equals("BLACK"))
                        cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.BISHOP, PawnColor.BLACK));
                    else
                        cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.BISHOP, PawnColor.WHITE));
                    break;
                } case ("QUEEN"): {
                    if (color.equals("BLACK"))
                        cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.QUEEN, PawnColor.BLACK));
                    else
                        cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.QUEEN, PawnColor.WHITE));
                    break;
                } case ("ROOK"): {
                    if (color.equals("BLACK"))
                        cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.ROOK, PawnColor.BLACK));
                    else
                        cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.ROOK, PawnColor.WHITE));
                    break;
                }

            }

        }

        Board.setBoard(cacheMap);

    }

    private void loadChessNotation(File dir) {
        String filePath = dir.getAbsolutePath();
        filePath = filePath + "/chessNotation.json";
        String json = null;
        try {
            json = new Scanner(new File(filePath)).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> cacheNotation= new Gson().fromJson(json, List.class);
        ChessNotation.setMovesList(cacheNotation);
    }

    private void loadComputer(File dir) {
        try {
            String filePath = dir.getAbsolutePath();
            filePath = filePath + "/computer.json";
            int skill = Integer.parseInt(new Scanner(new File(filePath)).next());
            Computer.setSkill(skill);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStatistics(File dir) {

        try {
            String filePath = dir.getAbsolutePath();
            filePath = filePath + "/statistics.json";
            Scanner s = new Scanner(new File(filePath)).useDelimiter("\n");
            String gamewin = s.next();
            gamewin = gamewin.substring(gamewin.indexOf(':')+1);
            Statistics.setGameWin(Integer.parseInt(gamewin));
            String gameloss = s.next();
            gameloss = gameloss.substring(gameloss.indexOf(':')+1);
            Statistics.setGameLoss(Integer.parseInt(gameloss));
            String gamedraw = s.next();
            gamedraw = gamedraw.substring(gamedraw.indexOf(':')+1);
            Statistics.setGameDraw(Integer.parseInt(gamedraw));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
