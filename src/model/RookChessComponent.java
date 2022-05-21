package model;

import view.ChessboardPoint;
import controller.ClickController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RookChessComponent extends ChessComponent {
    private static Image ROOK_WHITE;
    private static Image ROOK_BLACK;

    private Image rookImage;

    public void loadResource() throws IOException {
        if (ROOK_WHITE == null) {
            ROOK_WHITE = ImageIO.read(new File("./images/rook-white.png"));
        }

        if (ROOK_BLACK == null) {
            ROOK_BLACK = ImageIO.read(new File("./images/rook-black.png"));
        }
    }

    private void initiateRookImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                rookImage = ROOK_WHITE;
            } else if (color == ChessColor.BLACK) {
                rookImage = ROOK_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RookChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController listener, int size) {
        super(chessboardPoint, location, chessColor, listener, size);
        initiateRookImage(chessColor);
        if (chessColor == ChessColor.BLACK) {
            this.name = 'R';
        }
        else {
            this.name = 'r';
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

        if (!isSkip()){
            pointList.removeIf(this::checkAfterMove);
        }
        return pointList;
    }


    /**
     * 注意这个方法，每当窗体受到了形状的变化，或者是通知要进行绘图的时候，就会调用这个方法进行画图。
     *
     * @param g 可以类比于画笔
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(rookImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
    }
}
