package view;


import controller.GameController;
import model.*;
import controller.ClickController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chessboard extends JComponent {
    private static final int CHESSBOARD_SIZE = 8;
    private final ChessComponent[][] chessComponents = new ChessComponent[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    private ChessColor currentColor = ChessColor.WHITE;
    private int round;
    private final ClickController clickController = new ClickController(this);
    private GameController gameController;
    private final int CHESS_SIZE;
    protected ChessColor winner;
    public KingChessComponent whiteKing;
    public KingChessComponent blackKing;

    public Chessboard(int width, int height) {
        setLayout(null); // Use absolute layout.
        setSize(width, height);

        CHESS_SIZE = width / 8;
        System.out.printf("chessboard size = %d, chess size = %d\n", width, CHESS_SIZE);

        round = 0;
        initiateEmptyChessboard();

        initRookOnBoard(0, 0, ChessColor.BLACK);
        initRookOnBoard(0, CHESSBOARD_SIZE - 1, ChessColor.BLACK);
        initRookOnBoard(CHESSBOARD_SIZE - 1, 0, ChessColor.WHITE);
        initRookOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 1, ChessColor.WHITE);
        initKnightOnBoard(0, 1, ChessColor.BLACK);
        initKnightOnBoard(0, CHESSBOARD_SIZE - 2, ChessColor.BLACK);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, 1, ChessColor.WHITE);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 2, ChessColor.WHITE);
        initBishopOnBoard(0, 2, ChessColor.BLACK);
        initBishopOnBoard(0, CHESSBOARD_SIZE - 3, ChessColor.BLACK);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, 2, ChessColor.WHITE);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 3, ChessColor.WHITE);
        blackKing = initKingOnBoard(0, CHESSBOARD_SIZE - 4, ChessColor.BLACK);
        whiteKing = initKingOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 4, ChessColor.WHITE);
        initQueenOnBoard(0, 3, ChessColor.BLACK);
        initQueenOnBoard(CHESSBOARD_SIZE - 1, 3, ChessColor.WHITE);
        for (int i = 0; i < CHESSBOARD_SIZE; i++){
            initPawnOnBoard(1, i, ChessColor.BLACK);
            initPawnOnBoard(CHESSBOARD_SIZE - 2, i, ChessColor.WHITE);
        }
    }

    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

    public ChessColor getWinner() {
        return winner;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().getX(), col = chessComponent.getChessboardPoint().getY();

        if (chessComponents[row][col] != null) {
            remove(chessComponents[row][col]);
        }
        add(chessComponents[row][col] = chessComponent);
    }

    public void swapChessComponents(ChessComponent chess1, ChessComponent chess2) {
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        remove(chess2);
        add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE));
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;

        chess1.repaint();
        chess2.repaint();
    }

    public void removeChessComponents(ChessComponent chess) {
        remove(chess);
        add(chess = new EmptySlotComponent(chess.getChessboardPoint(), chess.getLocation(), clickController, CHESS_SIZE));
        chess.repaint();
    }

    public void initiateEmptyChessboard() {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE));
            }
        }
    }

    public void swapColor() {
        currentColor = currentColor == ChessColor.BLACK ? ChessColor.WHITE : ChessColor.BLACK;
    }

    private void initRookOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initKnightOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KnightChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initBishopOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new BishopChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initQueenOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private KingChessComponent initKingOnBoard(int row, int col, ChessColor color) {
        KingChessComponent chessComponent = new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
        return chessComponent;
    }

    private void initPawnOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new PawnChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }


    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }

    public void loadGame(List<String> chessData) {
        initiateEmptyChessboard();
        for (int i = 0; i < chessData.size() - 1; i++){
            for (int j = 0; j < 8; j++){
                switch (chessData.get(i).charAt(j)) {
                    case 'K' -> blackKing = initKingOnBoard(i, j, ChessColor.BLACK);
                    case 'k' -> whiteKing = initKingOnBoard(i, j, ChessColor.WHITE);
                    case 'Q' -> initQueenOnBoard(i, j, ChessColor.BLACK);
                    case 'q' -> initQueenOnBoard(i, j, ChessColor.WHITE);
                    case 'B' -> initBishopOnBoard(i, j, ChessColor.BLACK);
                    case 'b' -> initBishopOnBoard(i, j, ChessColor.WHITE);
                    case 'N' -> initKnightOnBoard(i, j, ChessColor.BLACK);
                    case 'n' -> initKnightOnBoard(i, j, ChessColor.WHITE);
                    case 'R' -> initRookOnBoard(i, j, ChessColor.BLACK);
                    case 'r' -> initRookOnBoard(i, j, ChessColor.WHITE);
                    case 'P' -> initPawnOnBoard(i, j, ChessColor.BLACK);
                    case 'p' -> initPawnOnBoard(i, j, ChessColor.WHITE);
                }
            }
        }
        this.repaint();
        if (chessData.get(chessData.size() - 1).equals("w")) {
            currentColor = ChessColor.WHITE;
        }
        else
            currentColor = ChessColor.BLACK;
        this.checkCheckmated();
    }

    public void pawnTurn (ChessComponent chess , int n) {
        final ChessColor chessColor = chess.getChessColor();
        final int X = chess.getChessboardPoint().getX();
        final int Y = chess.getChessboardPoint().getY();
        remove(chess);
        switch (n) {
            case 0 -> initQueenOnBoard(X,Y,chessColor);
            case 1 -> initBishopOnBoard(X,Y,chessColor);
            case 2 -> initKnightOnBoard(X,Y,chessColor);
            case 3 -> initRookOnBoard(X,Y,chessColor);
        }
        chessComponents[X][Y].repaint();
    }

    public void checkCheckmated(){
        if (currentColor == ChessColor.BLACK){
            whiteKing.setCheckmated(false);
            whiteKing.repaint();
            for (ChessComponent[] Chess : chessComponents) {
                for (ChessComponent chess : Chess){
                    if (chess.getChessColor() != currentColor &&
                            chess.getCanMoveChess(chessComponents).contains(blackKing.getChessboardPoint())){
                        blackKing.setCheckmated(true);
                        blackKing.repaint();
                        break;
                    }
                }
            }
        }
        else {
            blackKing.setCheckmated(false);
            blackKing.repaint();
            for (ChessComponent[] Chess : chessComponents) {
                for (ChessComponent chess : Chess){
                    if (chess.getChessColor() != currentColor &&
                            chess.getCanMoveChess(chessComponents).contains(whiteKing.getChessboardPoint())){
                        whiteKing.setCheckmated(true);
                        whiteKing.repaint();
                        break;
                    }
                }
            }
        }

    }

    public boolean checkGameOver() {
        ChessComponent[][] chessComponents = getChessComponents();
        ChessColor chessColor = getCurrentColor();
        for (ChessComponent[] Chess : chessComponents){
            for (ChessComponent chess : Chess){
                if (chess.getCanMoveChess(chessComponents).size() != 0 && chess.getChessColor() == chessColor){
                    return false;
                }
            }
        }
        winner = chessColor == ChessColor.BLACK? ChessColor.WHITE : ChessColor.BLACK;
        return true;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                str = str.concat(chessComponents[i][j].toString());
            }
            str = str.concat("\n");
        }
        if (currentColor == ChessColor.WHITE) str = str.concat("w\n");
        else str = str.concat("b\n");
        return str;
    }

    public List<String> toStringList() {
        String[] strings = toString().split("\n");
        System.out.println(strings.length);
        return new ArrayList<>(Arrays.asList(strings));
    }

}
