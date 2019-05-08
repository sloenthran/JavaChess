package pl.nogacz.chess.application;

import javax.swing.*;

/**
 * @author Dawid Nogacz on 07.05.2019
 */
public class EndGame {

    public EndGame(String message) {
        printDialog(message);
    }

    private void printDialog(String message) {
        JFrame frame = new JFrame();

        Object stringArray[] = { "New game", "Exit game" };

        int result = JOptionPane.showOptionDialog(frame,
                message,
                "Select option",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                stringArray,
                stringArray[0]);

        chooseAction(result);
    }

    private void chooseAction(int action) {
        switch (action) {
            case 1: { newGame(); break; }
            default: { System.exit(0); break; }
        }
    }

    private void newGame() {
        //TODO newGame

    }
}
