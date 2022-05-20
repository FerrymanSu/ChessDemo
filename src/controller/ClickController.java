package controller;


import model.*;
import view.Chessboard;
import view.ChessboardPoint;

import java.util.ArrayList;

public class ClickController {
    private final Chessboard chessboard;
    private final ChessComponent[][] chessComponents;
    private ArrayList<ChessboardPoint> points;
    private ChessComponent first;
    private PawnChessComponent passPawn;

    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
        this.points = new ArrayList<>();
        this.chessComponents = chessboard.getChessComponents();
    }

    public Chessboard getChessboard() {
        return chessboard;
    }

    public void onClick(ChessComponent chessComponent) {
        if (first == null) {
            if (handleFirst(chessComponent)) {
                chessComponent.setSelected(true);
                first = chessComponent;
                first.repaint();

                points = first.getCanMoveChess(chessComponents);
                for (ChessboardPoint point : points){
                    int X = point.getX();
                    int Y = point.getY();
                    chessComponents[X][Y].setCanMoved(true);
                    chessComponents[X][Y].repaint();
                }

                if (chessboard.getRound() == 0){
                    chessboard.getGameController().saveGameToFile(chessboard.toStringList());
                }
            }
        } else {
            if (first == chessComponent) { // 再次点击取消选取
                chessComponent.setSelected(false);
                ChessComponent recordFirst = first;
                first = null;
                recordFirst.repaint();

                for (ChessboardPoint point : points){
                    int X = point.getX();
                    int Y = point.getY();
                    chessComponents[X][Y].setCanMoved(false);
                    chessComponents[X][Y].repaint();
                    points = new ArrayList<>();
                }
            }
            else if (first.getChessColor() == chessComponent.getChessColor()){ // 切换选取目标
                first.setSelected(false);
                first.repaint();

                for (ChessboardPoint point : points){
                    int X = point.getX();
                    int Y = point.getY();
                    chessComponents[X][Y].setCanMoved(false);
                    chessComponents[X][Y].repaint();
                    points = new ArrayList<>();
                }

                chessComponent.setSelected(true);
                first = chessComponent;
                chessComponent.repaint();

                points = first.getCanMoveChess(chessComponents);
                for (ChessboardPoint point : points){
                    int X = point.getX();
                    int Y = point.getY();
                    chessComponents[X][Y].setCanMoved(true);
                    chessComponents[X][Y].repaint();
                }
            }
            else if (handleSecond(chessComponent)) {
                if (passPawn != null){
                    passPawn.setTwoStep(false);
                    passPawn = null;
                }
                if (first instanceof PawnChessComponent &&
                        Math.abs(chessComponent.getChessboardPoint().getX() - first.getChessboardPoint().getX()) == 2){
                    passPawn = ((PawnChessComponent) first);
                    passPawn.setTwoStep(true);
                }
                if (first instanceof PawnChessComponent && chessComponent instanceof EmptySlotComponent &&
                        first.getChessboardPoint().getY() != chessComponent.getChessboardPoint().getY()){
                    int X = first.getChessboardPoint().getX();
                    int Y = chessComponent.getChessboardPoint().getY();
                    chessboard.removeChessComponents(chessComponents[X][Y]);
                }
                //repaint in swap chess method.
                first.setSelected(false);
                chessboard.swapChessComponents(first, chessComponent);
                chessboard.swapColor();

                for (ChessboardPoint point : points){
                    int X = point.getX();
                    int Y = point.getY();
                    chessComponents[X][Y].setCanMoved(false);
                    chessComponents[X][Y].repaint();
                }

                chessboard.checkCheckmated();
                first = null;

                System.out.print(chessboard);
                chessboard.setRound(chessboard.getRound() + 1);
                chessboard.getGameController().saveGameToFile(chessboard.toStringList());

                if (chessboard.checkGameOver()){
                    System.out.println(chessboard.getWinner());
                }
            }
        }
    }

    /**
     * @param chessComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */

    private boolean handleFirst(ChessComponent chessComponent) {
        return chessComponent.getChessColor() == chessboard.getCurrentColor();
    }

    /**
     * @param chessComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */

    private boolean handleSecond(ChessComponent chessComponent) {
        return chessComponent.getChessColor() != chessboard.getCurrentColor() &&
                first.canMoveTo(chessboard.getChessComponents(), chessComponent.getChessboardPoint());
    }
}
