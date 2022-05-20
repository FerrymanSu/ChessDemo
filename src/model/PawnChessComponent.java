package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PawnChessComponent extends ChessComponent {
    private static Image PAWN_WHITE;
    private static Image PAWN_BLACK;
    private Image pawnImage;
    private boolean twoStep;

    public void loadResource() throws IOException {
        if (PAWN_WHITE == null) {
            PAWN_WHITE = ImageIO.read(new File("./images/pawn-white.png"));
        }

        if (PAWN_BLACK == null) {
            PAWN_BLACK = ImageIO.read(new File("./images/pawn-black.png"));
        }
    }

    private void initiatePawnImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                pawnImage = PAWN_WHITE;
            } else if (color == ChessColor.BLACK) {
                pawnImage = PAWN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isTwoStep() {
        return twoStep;
    }

    public void setTwoStep(boolean twoStep) {
        this.twoStep = twoStep;
    }

    public PawnChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
        initiatePawnImage(chessColor);
        if (chessColor == ChessColor.BLACK) {
            this.name = 'P';
        }
        else {
            this.name = 'p';
        }
    }

    @Override
    public ArrayList<ChessboardPoint> getCanMoveChess(ChessComponent[][] chessboard) {
        ArrayList<ChessboardPoint> pointList = new ArrayList<>();
        int X = getChessboardPoint().getX();
        int Y = getChessboardPoint().getY();
        ChessColor chessColor = getChessColor();
        boolean move = false;

        if (this.getChessColor() == ChessColor.WHITE){
            if (X > 0){
                if (chessboard[X - 1][Y] instanceof EmptySlotComponent){
                    pointList.add(chessboard[X - 1][Y].getChessboardPoint());
                    move = true;
                }
                if (X == 6 && move && chessboard[X - 2][Y] instanceof EmptySlotComponent){
                    pointList.add(chessboard[X - 2][Y].getChessboardPoint());
                }
                if (Y > 0 && !(chessboard[X - 1][Y - 1] instanceof EmptySlotComponent)
                        && chessboard[X - 1][Y - 1].getChessColor() != chessColor){
                    pointList.add(chessboard[X - 1][Y - 1].getChessboardPoint());
                }
                if (Y < 7 && !(chessboard[X - 1][Y + 1] instanceof EmptySlotComponent
                        && chessboard[X - 1][Y + 1].getChessColor() != chessColor)){
                    pointList.add(chessboard[X - 1][Y + 1].getChessboardPoint());
                }
                if (Y > 0 && X == 3 && chessboard[3][Y - 1] instanceof PawnChessComponent
                        && ((PawnChessComponent) chessboard[3][Y - 1]).isTwoStep()){
                    pointList.add(chessboard[2][Y - 1].getChessboardPoint());
                }
            }
        }
        else {
            if (X < 7){
                if (chessboard[X + 1][Y] instanceof EmptySlotComponent){
                    pointList.add(chessboard[X + 1][Y].getChessboardPoint());
                    move = true;
                }
                if (X == 1 && move && chessboard[X + 2][Y] instanceof EmptySlotComponent){
                    pointList.add(chessboard[X + 2][Y].getChessboardPoint());
                }
                if (Y > 0 && !(chessboard[X + 1][Y - 1] instanceof EmptySlotComponent)
                        && chessboard[X + 1][Y - 1].getChessColor() != chessColor){
                    pointList.add(chessboard[X + 1][Y - 1].getChessboardPoint());
                }
                if (Y < 7 && !(chessboard[X + 1][Y + 1] instanceof EmptySlotComponent)
                        && chessboard[X + 1][Y + 1].getChessColor() != chessColor){
                    pointList.add(chessboard[X + 1][Y + 1].getChessboardPoint());
                }
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
        g.drawImage(pawnImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
    }

}
