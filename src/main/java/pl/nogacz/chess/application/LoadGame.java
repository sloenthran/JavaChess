package pl.nogacz.chess.application;


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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.zip.CRC32;

public class LoadGame {

    private File dir;
    public void load(){
        DirectoryChooser dirChooser = new DirectoryChooser();

        dirChooser.setTitle("Choose location");

        File selectedDir = dirChooser.showDialog(new Stage());
        this.dir = selectedDir;
        try {
            loadFromDirectory(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFromDirectory(File selectedDir) throws FileNotFoundException,IllegalStateException {
        this.dir = selectedDir;
        if(selectedDir != null) {
            loadBoard();
            loadChessNotation();
            loadComputer();
            loadStatistics();
        }

    }

    private void loadBoard() throws FileNotFoundException, IllegalStateException {
        Board.clearBoard();
        String filePath = dir.getAbsolutePath();
        filePath = filePath + "/board.txt";
        Scanner sc = null;
        sc = new Scanner(new FileInputStream(filePath));
        HashMap<Coordinates, PawnClass> cacheMap = new HashMap<>();
        String buffer = "";
        while(sc.hasNext()){
            String line = sc.next();
            if(line.contains(",")) {
                buffer += line;
                String[] tokens = line.split(",");
                int Xcoordinate = Integer.parseInt(tokens[0]);
                int Ycoordinate = Integer.parseInt(tokens[1]);
                String color = tokens[2];
                String pawnstr = tokens[3];
                switch (pawnstr) {
                    case ("PAWN"): {
                        if (color.equals("BLACK"))
                            cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
                        else
                            cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
                        break;
                    }
                    case ("KNIGHT"): {
                        if (color.equals("BLACK"))
                            cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.KNIGHT, PawnColor.BLACK));
                        else
                            cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.KNIGHT, PawnColor.WHITE));
                        break;
                    }
                    case ("KING"): {
                        if (color.equals("BLACK"))
                            cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.KING, PawnColor.BLACK));
                        else
                            cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.KING, PawnColor.WHITE));
                        break;
                    }
                    case ("BISHOP"): {
                        if (color.equals("BLACK"))
                            cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.BISHOP, PawnColor.BLACK));
                        else
                            cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.BISHOP, PawnColor.WHITE));
                        break;
                    }
                    case ("QUEEN"): {
                        if (color.equals("BLACK"))
                            cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.QUEEN, PawnColor.BLACK));
                        else
                            cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.QUEEN, PawnColor.WHITE));
                        break;
                    }
                    case ("ROOK"): {
                        if (color.equals("BLACK"))
                            cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.ROOK, PawnColor.BLACK));
                        else
                            cacheMap.put(new Coordinates(Xcoordinate, Ycoordinate), new PawnClass(Pawn.ROOK, PawnColor.WHITE));
                        break;
                    }

                }
            }else{
                boolean isCRCcorrect = CRCcheck(buffer.getBytes(), Long.parseLong(line));
                if(!isCRCcorrect){
                    throw new IllegalStateException("Board file is not trusted.");
                }
            }

        }


        Board.setBoard(cacheMap);
        Board.redesignBoard();
    }

    private void loadChessNotation() throws IllegalStateException, FileNotFoundException {
        String filePath = dir.getAbsolutePath();
        filePath = filePath + "/chessNotation.txt";
        Scanner sc = null;
        sc = new Scanner(new FileInputStream(filePath)).useDelimiter("\n");
        List<String> cacheNotation= new ArrayList<>();
        String buffer = "";
        while(sc.hasNext()){
            String line = sc.next();
            if(line.contains(" ")) {
                buffer += line;
                cacheNotation.add(line);
            }
            else{
                boolean isCRCcorrect = CRCcheck(buffer.getBytes(), Long.parseLong(line));
                if(!isCRCcorrect){
                    throw new IllegalStateException("Chess notation file is not trusted.");
                }
            }
        }
        ChessNotation.setMovesList(cacheNotation);
    }

    private void loadComputer() throws IllegalStateException, FileNotFoundException {
        String filePath = dir.getAbsolutePath();
        filePath = filePath + "/computer.txt";
        Scanner sc = new Scanner(new File(filePath));
        String strSkill = sc.next();
        long crc = Long.parseLong(sc.next());
        boolean isCRCcorrect = CRCcheck(strSkill.getBytes(), crc);
        if(!isCRCcorrect){
            throw new IllegalStateException("Computer file is not trusted.");
        }
        Computer.setSkill(Integer.parseInt(strSkill));

    }

    private void loadStatistics() throws IllegalStateException, FileNotFoundException {


        String filePath = dir.getAbsolutePath();
        filePath = filePath + "/statistics.txt";
        String buffer;
        Scanner s = new Scanner(new File(filePath)).useDelimiter("\n");
        String gamewin = s.next();
        buffer = gamewin + "\n";
        gamewin = gamewin.substring(gamewin.indexOf(':')+1);
        Statistics.setGameWin(Integer.parseInt(gamewin));
        String gameloss = s.next();
        buffer = buffer + gameloss + "\n";
        gameloss = gameloss.substring(gameloss.indexOf(':')+1);
        Statistics.setGameLoss(Integer.parseInt(gameloss));
        String gamedraw = s.next();
        buffer = buffer + gamedraw ;
        gamedraw = gamedraw.substring(gamedraw.indexOf(':')+1);
        Statistics.setGameDraw(Integer.parseInt(gamedraw));

        long crc = Long.parseLong(s.next());
        boolean isCRCcorrect = CRCcheck(buffer.getBytes(), crc);
        if(!isCRCcorrect){
            throw new IllegalStateException("Statistics file is not trusted.");
        }


    }

    private boolean CRCcheck(byte[] byteArr1, long crc){
        CRC32 crcmaker = new CRC32();
        crcmaker.update(byteArr1, 0, byteArr1.length);
        long value1 = crcmaker.getValue();
        return value1 == crc;
    }
}
