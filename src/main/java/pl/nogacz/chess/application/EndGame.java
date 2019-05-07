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

        Object stringArray[] = { "Do It", "No Way" };
        JOptionPane.showOptionDialog(frame, message, "Select an Option",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray,
                stringArray[0]);
    }
}
