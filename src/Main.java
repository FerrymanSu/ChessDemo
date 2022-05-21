import view.ChessGameFrame;
import view.FirstView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        /*SwingUtilities.invokeLater(() -> {
            ChessGameFrame mainFrame = new ChessGameFrame(1000, 760);
            mainFrame.setVisible(true);
        });*/
        FirstView firstView = new FirstView();
        firstView.setVisible(true);
    }
}
