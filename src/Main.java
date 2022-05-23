import view.ChessGameFrame;
import view.FirstView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FirstView firstView = new FirstView();
            firstView.setVisible(true);
        });
    }
}
