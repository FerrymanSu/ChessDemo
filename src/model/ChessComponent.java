package model;

import AI.ChessAI;
import view.Chessboard;
import view.ChessboardPoint;
import controller.ClickController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class ChessComponent extends JComponent implements Cloneable{

    private  Color[] BACKGROUND_COLORS = {new Color(103,67,49), new Color(231,203,167)};
    private ClickController clickController;

    private ChessboardPoint chessboardPoint;
    protected final ChessColor chessColor;
    protected char name;
    private boolean move;
    private boolean selected;
    private boolean canMoved;
    private boolean skip;
    private boolean mouseEnter;
    protected ChessComponent[][] chessComponents;

    public void setColor(Color color1, Color color2){
        BACKGROUND_COLORS[0] = color1;
        BACKGROUND_COLORS[1] = color2;
    }

    protected ChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setLocation(location);
        setSize(size, size);
        this.chessboardPoint = chessboardPoint;
        this.chessColor = chessColor;
        this.move = false;
        this.selected = false;
        this.canMoved = false;
        this.skip = false;
        this.mouseEnter = false;
        this.clickController = clickController;
        this.chessComponents = clickController.getChessboard().getChessComponents();
    }

    public ChessboardPoint getChessboardPoint() {
        return chessboardPoint;
    }

    public void setChessboardPoint(ChessboardPoint chessboardPoint) {
        this.chessboardPoint = chessboardPoint;
    }

    public ChessColor getChessColor() {
        return chessColor;
    }

    public boolean isMove() {
        return move;
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isCanMoved() {
        return canMoved;
    }

    public void setCanMoved(boolean canMoved) {
        this.canMoved = canMoved;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public boolean isMouseEnter() {
        return mouseEnter;
    }

    public char Name() {
        return name;
    }

    public void setMouseEnter(boolean mouseEnter) {
        this.mouseEnter = mouseEnter;
    }

    /**
     * @param another ?????????????????????????????????????????????
     *                <br>
     *                ???????????????????????????????????????????????????????????????????????????????????????(EmptySlotComponent)?????????
     */
    public void swapLocation(ChessComponent another) {
        ChessboardPoint chessboardPoint1 = getChessboardPoint(), chessboardPoint2 = another.getChessboardPoint();
        Point point1 = getLocation(), point2 = another.getLocation();
        setChessboardPoint(chessboardPoint2);
        setLocation(point2);
        another.setChessboardPoint(chessboardPoint1);
        another.setLocation(point1);
    }

    /**
     * @param e ????????????????????????
     *          <br>
     *          ????????????????????????????????????????????????????????????????????????????????????????????????onClick??????????????????????????????????????????????????????
     */
    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        Chessboard chessboard = clickController.getChessboard();
        boolean AI = chessboard.isAI();
        if (!AI){
            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                System.out.printf("Click [%d,%d]\n", chessboardPoint.getX(), chessboardPoint.getY());
                clickController.onClick(this);
            }
            if (e.getID() == MouseEvent.MOUSE_ENTERED) {
                setMouseEnter(true);
                this.repaint();
            }
            if (e.getID() == MouseEvent.MOUSE_EXITED) {
                setMouseEnter(false);
                this.repaint();
            }
        }
        else {
            if (chessboard.getCurrentColor() == ChessColor.WHITE) {
                if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                    clickController.onClick(this);
                }
                if (e.getID() == MouseEvent.MOUSE_ENTERED) {
                    setMouseEnter(true);
                    this.repaint();
                }
                if (e.getID() == MouseEvent.MOUSE_EXITED) {
                    setMouseEnter(false);
                    this.repaint();
                }
            }
        }

    }

    public abstract ArrayList<ChessboardPoint> getCanMoveChess(ChessComponent[][] chessboard);

    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination){
        ArrayList<ChessboardPoint> pointsList = this.getCanMoveChess(chessboard);
        return pointsList.contains(destination);
    }

    public boolean checkAfterMove(ChessboardPoint chessboardPoint){
        KingChessComponent king = null;
        ChessComponent[][] chessComponents = new ChessComponent[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessComponents[i][j] = this.chessComponents[i][j].clone();
            }
        }
        for (ChessComponent[] Chess : chessComponents){
            for (ChessComponent chess : Chess) {
                if (chess instanceof KingChessComponent && chessColor == chess.getChessColor()){
                    king = (KingChessComponent) chess;
                    break;
                }
            }
        }
        int x1 = this.chessboardPoint.getX();
        int y1 = this.chessboardPoint.getY();
        ChessComponent chess1 = chessComponents[x1][y1];
        int x2 = chessboardPoint.getX();
        int y2 = chessboardPoint.getY();
        ChessComponent chess2 = chessComponents[x2][y2];

        chess1.setChessboardPoint(new ChessboardPoint(x2,y2));
        chessComponents[x2][y2] = chess1;
        chessComponents[x1][y1] =
                new EmptySlotComponent(this.chessboardPoint, new Point(0,0), clickController, 0);
        
        for (ChessComponent[] Chess : chessComponents){
            for (ChessComponent chess : Chess) {
                if (chess.getChessColor() != chessColor) {
                    chess.setSkip(true);
                    ArrayList<ChessboardPoint> chessboardPoints = chess.getCanMoveChess(chessComponents);
                    chess.setSkip(false);

                    assert king != null;
                    if (chessboardPoints.contains(king.getChessboardPoint())){
                        chess1.setChessboardPoint(new ChessboardPoint(x1,y1));
                        chessComponents[x1][y1] = chess1;
                        chessComponents[x2][y2] = chess2;
                        return true;
                    }
                }
            }
        }
        chess1.setChessboardPoint(this.chessboardPoint);
        chessComponents[x1][y1] = chess1;
        chessComponents[x2][y2] = chess2;
        return false;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????
     *
     * @throws IOException ???????????????????????????(???????????????????????????)?????????????????????
     */
    public abstract void loadResource() throws IOException;

    @Override
    public String toString() {
        return String.valueOf(this.name);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        Color squareColor = BACKGROUND_COLORS[(chessboardPoint.getX() + chessboardPoint.getY()) % 2];
        g.setColor(squareColor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (isMouseEnter()) {
            g.setColor(new Color(51,201,255));
            g.draw3DRect(0,0,this.getWidth(),this.getHeight(),true);
            g.fill3DRect(0,0,this.getWidth(),this.getHeight(),true);
        }
        if (isSelected()) {
            // Highlights the model if selected.
            g.setColor(Color.LIGHT_GRAY);
            g.drawRect(0, 0, getWidth() , getHeight());
            g.fillRect(0, 0, getWidth() , getHeight());
        }
        if (isCanMoved()) {
            g.setColor(Color.GRAY);
            g.fillOval(getWidth()/3, getHeight()/3, getWidth()/3, getHeight()/3);
        }
    }

    @Override
    public ChessComponent clone() {
        try {
            ChessComponent clone = (ChessComponent) super.clone();
            clone.setChessboardPoint(new ChessboardPoint(chessboardPoint.getX(),chessboardPoint.getY()));
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}