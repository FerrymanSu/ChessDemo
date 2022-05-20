package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class KnightChessComponent extends ChessComponent {
    private static Image KNIGHT_WHITE;
    private static Image KNIGHT_BLACK;
    private Image knightImage;

    public void loadResource() throws IOException {
        if (KNIGHT_WHITE == null) {
            KNIGHT_WHITE = ImageIO.read(new File("./images/knight-white.png"));
        }

        if (KNIGHT_BLACK == null) {
            KNIGHT_BLACK = ImageIO.read(new File("./images/knight-black.png"));
        }
    }

    private void initiateKnightImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                knightImage = KNIGHT_WHITE;
            } else if (color == ChessColor.BLACK) {
                knightImage = KNIGHT_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public KnightChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
        initiateKnightImage(chessColor);
        if (chessColor == ChessColor.BLACK) {
            this.name = 'N';
        }
        else {
            this.name = 'n';
        }
    }

    @Override
    public ArrayList<ChessboardPoint> getCanMoveChess(ChessComponent[][] chessboard) {
        ArrayList<ChessboardPoint> pointList = new ArrayList<>();
        int X = getChessboardPoint().getX();
        int Y = getChessboardPoint().getY();
        ChessColor chessColor = getChessColor();

        if (X - 2 >= 0 && Y - 1 >= 0 && chessboard[X - 2][Y - 1].getChessColor() != chessColor)
            pointList.add(chessboard[X - 2][Y - 1].getChessboardPoint());
        if (X - 1 >= 0 && Y - 2 >= 0 && chessboard[X - 1][Y - 2].getChessColor() != chessColor)
            pointList.add(chessboard[X - 1][Y - 2].getChessboardPoint());
        if (X + 2 <= 7 && Y - 1 >= 0 && chessboard[X + 2][Y - 1].getChessColor() != chessColor)
            pointList.add(chessboard[X + 2][Y - 1].getChessboardPoint());
        if (X + 1 <= 7 && Y - 2 >= 0 && chessboard[X + 1][Y - 2].getChessColor() != chessColor)
            pointList.add(chessboard[X + 1][Y - 2].getChessboardPoint());
        if (X - 2 >= 0 && Y + 1 <= 7 && chessboard[X - 2][Y + 1].getChessColor() != chessColor)
            pointList.add(chessboard[X - 2][Y + 1].getChessboardPoint());
        if (X - 1 >= 0 && Y + 2 <= 7 && chessboard[X - 1][Y + 2].getChessColor() != chessColor)
            pointList.add(chessboard[X - 1][Y + 2].getChessboardPoint());
        if (X + 1 <= 7 && Y + 2 <= 7 && chessboard[X + 1][Y + 2].getChessColor() != chessColor)
            pointList.add(chessboard[X + 1][Y + 2].getChessboardPoint());
        if (X + 2 <= 7 && Y + 1 <= 7 && chessboard[X + 2][Y + 1].getChessColor() != chessColor)
            pointList.add(chessboard[X + 2][Y + 1].getChessboardPoint());

        if (!isSkip()){
            pointList.removeIf(this::checkAfterMove);
        }
        return pointList;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(knightImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
    }
}
