package model;

import view.ChessboardPoint;
import controller.ClickController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public abstract class ChessComponent extends JComponent implements Cloneable{

    private static final Color[] BACKGROUND_COLORS = {Color.WHITE, Color.BLACK};
    private ClickController clickController;

    private ChessboardPoint chessboardPoint;
    protected final ChessColor chessColor;
    protected char name;
    private boolean selected;
    private boolean canMoved;
    private boolean skip;
    private boolean mouseEnter;
    protected ChessComponent[][] chessComponents;

    protected ChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setLocation(location);
        setSize(size, size);
        this.chessboardPoint = chessboardPoint;
        this.chessColor = chessColor;
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



    public void setMouseEnter(boolean mouseEnter) {
        this.mouseEnter = mouseEnter;
    }

    /**
     * @param another 主要用于和另外一个棋子交换位置
     *                <br>
     *                调用时机是在移动棋子的时候，将操控的棋子和对应的空位置棋子(EmptySlotComponent)做交换
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
     * @param e 响应鼠标监听事件
     *          <br>
     *          当接收到鼠标动作的时候，这个方法就会自动被调用，调用所有监听者的onClick方法，处理棋子的选中，移动等等行为。
     */
    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);

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
     * 这个方法主要用于加载一些特定资源，如棋子图片等等。
     *
     * @throws IOException 如果一些资源找不到(如棋子图片路径错误)，就会抛出异常
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
            g.setColor(Color.GREEN);
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