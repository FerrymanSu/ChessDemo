package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class QueenChessComponent extends ChessComponent {
    private static Image QUEEN_WHITE;
    private static Image QUEEN_BLACK;
    private Image queenImage;

    public void loadResource() throws IOException {
        if (QUEEN_WHITE == null) {
            QUEEN_WHITE = ImageIO.read(new File("./images/queen-white.png"));
        }

        if (QUEEN_BLACK == null) {
            QUEEN_BLACK = ImageIO.read(new File("./images/queen-black.png"));
        }
    }

    private void initiateQueenImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                queenImage = QUEEN_WHITE;
            } else if (color == ChessColor.BLACK) {
                queenImage = QUEEN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public QueenChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
        initiateQueenImage(chessColor);
        if (chessColor == ChessColor.BLACK) {
            this.name = 'Q';
        }
        else {
            this.name = 'q';
        }
    }

    @Override
    public ArrayList<ChessboardPoint> getCanMoveChess(ChessComponent[][] chessboard) {
        ArrayList<ChessboardPoint> pointList = new ArrayList<>();
        int X = getChessboardPoint().getX();
        int Y = getChessboardPoint().getY();
        ChessColor chessColor = getChessColor();

        for (int i = 1 ; X - i >= 0; i++){
            if (chessboard[X - i][Y].getChessColor() == chessColor)
                break;
            pointList.add(chessboard[X - i][Y].getChessboardPoint());
            if (!(chessboard[X - i][Y] instanceof EmptySlotComponent))
                break;
        }
        for (int i = 1 ; X + i <= 7; i++){
            if (chessboard[X + i][Y].getChessColor() == chessColor)
                break;
            pointList.add(chessboard[X + i][Y].getChessboardPoint());
            if (!(chessboard[X + i][Y] instanceof EmptySlotComponent))
                break;
        }
        for (int i = 1 ; Y - i >= 0; i++){
            if (chessboard[X][Y - i].getChessColor() == chessColor)
                break;
            pointList.add(chessboard[X][Y - i].getChessboardPoint());
            if (!(chessboard[X][Y - i] instanceof EmptySlotComponent))
                break;
        }
        for (int i = 1 ; Y + i <= 7; i++){
            if (chessboard[X][Y + i].getChessColor() == chessColor)
                break;
            pointList.add(chessboard[X][Y + i].getChessboardPoint());
            if (!(chessboard[X][Y + i] instanceof EmptySlotComponent))
                break;
        }
        for (int i = 1 ; X - i >= 0 && Y - i >= 0; i++){
            if (chessboard[X - i][Y - i].getChessColor() == chessColor)
                break;
            pointList.add(chessboard[X - i][Y - i].getChessboardPoint());
            if (!(chessboard[X - i][Y - i] instanceof EmptySlotComponent))
                break;
        }
        for (int i = 1 ; X + i <= 7 && Y - i >= 0; i++){
            if (chessboard[X + i][Y - i].getChessColor() == chessColor)
                break;
            pointList.add(chessboard[X + i][Y - i].getChessboardPoint());
            if (!(chessboard[X + i][Y - i] instanceof EmptySlotComponent))
                break;
        }
        for (int i = 1 ; X - i >= 0 && Y + i <= 7; i++){
            if (chessboard[X - i][Y + i].getChessColor() == chessColor)
                break;
            pointList.add(chessboard[X - i][Y + i].getChessboardPoint());
            if (!(chessboard[X - i][Y + i] instanceof EmptySlotComponent))
                break;
        }
        for (int i = 1 ; X + i <= 7 && Y + i <= 7; i++){
            if (chessboard[X + i][Y + i].getChessColor() == chessColor)
                break;
            pointList.add(chessboard[X + i][Y + i].getChessboardPoint());
            if (!(chessboard[X + i][Y + i] instanceof EmptySlotComponent))
                break;
        }

        if (!isSkip()){
            pointList.removeIf(this::checkAfterMove);
        }
        return pointList;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(queenImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
    }

}
