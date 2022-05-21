package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class KingChessComponent extends ChessComponent {
    private static Image KING_WHITE;
    private static Image KING_BLACK;
    private Image kingImage;
    private boolean checkmated;

    public void loadResource() throws IOException {
        if (KING_WHITE == null) {
            KING_WHITE = ImageIO.read(new File("./images/king-white.png"));
        }

        if (KING_BLACK == null) {
            KING_BLACK = ImageIO.read(new File("./images/king-black.png"));
        }
    }

    private void initiateKingImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                kingImage = KING_WHITE;
            } else if (color == ChessColor.BLACK) {
                kingImage = KING_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KingChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
        initiateKingImage(chessColor);
        if (chessColor == ChessColor.BLACK) {
            this.name = 'K';
        }
        else {
            this.name = 'k';
        }
        this.checkmated = false;
    }

    public void setCheckmated(boolean checkmated) {
        this.checkmated = checkmated;
    }

    @Override
    public ArrayList<ChessboardPoint> getCanMoveChess(ChessComponent[][] chessboard) {
        ArrayList<ChessboardPoint> pointList = new ArrayList<>();
        int X = getChessboardPoint().getX();
        int Y = getChessboardPoint().getY();
        ChessColor chessColor = getChessColor();

        if (X > 0 && chessboard[X - 1][Y].getChessColor() != chessColor)
            pointList.add(chessboard[X - 1][Y].getChessboardPoint());
        if (Y > 0 && chessboard[X][Y - 1].getChessColor() != chessColor)
            pointList.add(chessboard[X][Y - 1].getChessboardPoint());
        if (X < 7 && chessboard[X + 1][Y].getChessColor() != chessColor)
            pointList.add(chessboard[X + 1][Y].getChessboardPoint());
        if (Y < 7 && chessboard[X][Y + 1].getChessColor() != chessColor)
            pointList.add(chessboard[X][Y + 1].getChessboardPoint());
        if (X > 0 && Y > 0 && chessboard[X - 1][Y - 1].getChessColor() != chessColor)
            pointList.add(chessboard[X - 1][Y - 1].getChessboardPoint());
        if (X > 0 && Y < 7 && chessboard[X - 1][Y + 1].getChessColor() != chessColor)
            pointList.add(chessboard[X - 1][Y + 1].getChessboardPoint());
        if (X < 7 && Y > 0 && chessboard[X + 1][Y - 1].getChessColor() != chessColor)
            pointList.add(chessboard[X + 1][Y - 1].getChessboardPoint());
        if (X < 7 && Y < 7 && chessboard[X + 1][Y + 1].getChessColor() != chessColor)
            pointList.add(chessboard[X + 1][Y + 1].getChessboardPoint());

        if (chessColor == ChessColor.WHITE && !isMove() &&
                chessboard[7][0] instanceof RookChessComponent && !chessboard[7][0].isMove()) {
            boolean b = true;
            for (int i = 1; i < 4; i++) {
                if (!(chessboard[7][i] instanceof EmptySlotComponent)) {
                    b = false;
                    break;
                }
            }
            if (b) {
                pointList.add(chessboard[7][2].getChessboardPoint());
            }
        }
        if (chessColor == ChessColor.WHITE && !isMove() &&
                chessboard[7][7] instanceof RookChessComponent && !chessboard[7][0].isMove()) {
            boolean b = true;
            for (int i = 5; i < 7; i++) {
                if (!(chessboard[7][i] instanceof EmptySlotComponent)) {
                    b = false;
                    break;
                }
            }
            if (b) {
                pointList.add(chessboard[7][6].getChessboardPoint());
            }
        }
        if (chessColor == ChessColor.BLACK && !isMove() &&
                chessboard[0][0] instanceof RookChessComponent && !chessboard[0][0].isMove()) {
            boolean b = true;
            for (int i = 1; i < 4; i++) {
                if (!(chessboard[0][i] instanceof EmptySlotComponent)) {
                    b = false;
                    break;
                }
            }
            if (b) {
                pointList.add(chessboard[0][2].getChessboardPoint());
            }
        }
        if (chessColor == ChessColor.BLACK && !isMove() &&
                chessboard[0][7] instanceof RookChessComponent && !chessboard[0][7].isMove()) {
            boolean b = true;
            for (int i = 5; i < 7; i++) {
                if (!(chessboard[0][i] instanceof EmptySlotComponent)) {
                    b = false;
                    break;
                }
            }
            if (b) {
                pointList.add(chessboard[0][6].getChessboardPoint());
            }
        }

        if (!isSkip()){
            pointList.removeIf(this::checkAfterMove);
        }
        return pointList;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (checkmated && !isSelected()){
            System.out.printf("The player %s is checkmated!\n",chessColor);
            g.setColor(Color.RED);
            g.drawRect(0, 0, getWidth(), getHeight());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        g.drawImage(kingImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
    }
}
