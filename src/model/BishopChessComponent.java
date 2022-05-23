package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BishopChessComponent extends ChessComponent {
    private static Image BISHOP_WHITE;
    private static Image BISHOP_BLACK;
    private Image bishopImage;

    public void loadResource() throws IOException {
        if (BISHOP_WHITE == null) {
            BISHOP_WHITE = ImageIO.read(new File("./images/bishop-white.png"));
        }

        if (BISHOP_BLACK == null) {
            BISHOP_BLACK = ImageIO.read(new File("./images/bishop-black.png"));
        }
    }

    private void initiateBishopImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                bishopImage = BISHOP_WHITE;
            } else if (color == ChessColor.BLACK) {
                bishopImage = BISHOP_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BishopChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
        initiateBishopImage(chessColor);
        if (chessColor == ChessColor.BLACK) {
            this.name = 'B';
        }
        else {
            this.name = 'b';
        }
    }

    @Override
    public ArrayList<ChessboardPoint> getCanMoveChess(ChessComponent[][] chessboard) {
        ArrayList<ChessboardPoint> pointList = new ArrayList<>();
        int X = getChessboardPoint().getX();
        int Y = getChessboardPoint().getY();
        ChessColor chessColor = getChessColor();

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
        g.drawImage(bishopImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
    }
}
