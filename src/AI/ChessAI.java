package AI;

import controller.ClickController;
import model.ChessColor;
import model.ChessComponent;
import model.EmptySlotComponent;
import view.Chessboard;
import view.ChessboardPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ChessAI implements Cloneable{
    private final Chessboard chessboard;
    private final ClickController clickController;
    private AILevel level;
    private final ChessComponent[][] chessComponents;
    private final HashMap<Character,Integer> hashMap;
    private int depth;
    private ChessboardPoint chessboardPoint1;
    private ChessboardPoint chessboardPoint2;

    public ChessAI(Chessboard chessboard, AILevel level) {
        this.chessboard = chessboard;
        this.chessComponents = chessboard.getChessComponents();
        this.clickController = chessboard.getClickController();
        this.level = level;
        hashMap = new HashMap<>();
        hashMap.put('_', 0);
        hashMap.put('P', 10);
        hashMap.put('p', -10);
        hashMap.put('N', 30);
        hashMap.put('n', -30);
        hashMap.put('B', 30);
        hashMap.put('b', -30);
        hashMap.put('R', 50);
        hashMap.put('r', -50);
        hashMap.put('Q', 90);
        hashMap.put('q', -90);
        hashMap.put('K', 900);
        hashMap.put('k', -900);
    }

    public Chessboard getChessboard() {
        return chessboard;
    }

    public ClickController getClickController() {
        return clickController;
    }

    public AILevel getLevel() {
        return level;
    }

    public void setLevel(AILevel level) {
        this.level = level;
    }

    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }

    public void playChess () {
        if (level == AILevel.Random || level == AILevel.Greedy) {
            ChessComponent chess;
            clickController.onClick(chess = selectSource());
            clickController.onClick(selectTarget(chess));
        }
        else {
            if (level == AILevel.Simple) {
                this.depth = 2;
            }
            if (level == AILevel.Hard) {
                this.depth = 4;
            }
            chessFunction(chessComponents, depth,-9999,9999,ChessColor.BLACK);
            if (chessboardPoint1 == null || chessboardPoint2 == null) {
                this.setLevel(AILevel.Random);
                System.out.println("random");
                this.playChess();
                this.setLevel(AILevel.Simple);
            }
            else {
                clickController.onClick(chessComponents[chessboardPoint1.getX()][chessboardPoint1.getY()]);
                clickController.onClick(chessComponents[chessboardPoint2.getX()][chessboardPoint2.getY()]);
                chessboardPoint1 = null;
                chessboardPoint2 = null;
            }


        }
    }

    public ChessComponent selectSource() {
        if (level == AILevel.Random) {
            Random random = new Random();
            while (true) {
                int X = random.nextInt(8);
                int Y = random.nextInt(8);
                ChessComponent chessComponent = chessComponents[X][Y];
                if (chessComponent.getChessColor() == ChessColor.BLACK &&
                        chessComponent.getCanMoveChess(chessComponents).size() != 0) {
                    return chessComponent;
                }
            }
        }
        if (level == AILevel.Greedy) {
            for (ChessComponent[] Chess : chessComponents) {
                for (ChessComponent chess : Chess) {
                    if (chess.getChessColor() == ChessColor.BLACK) {
                        ArrayList<ChessboardPoint> points = chess.getCanMoveChess(chessComponents);
                        for (ChessboardPoint point : points) {
                            int X = point.getX();
                            int Y = point.getY();
                            if (!(chessComponents[X][Y] instanceof EmptySlotComponent)) {
                                return chess;
                            }
                        }
                    }
                }
            }
            Random random = new Random();
            while (true) {
                int X = random.nextInt(8);
                int Y = random.nextInt(8);
                ChessComponent chessComponent = chessComponents[X][Y];
                if (chessComponent.getChessColor() == ChessColor.BLACK &&
                        chessComponent.getCanMoveChess(chessComponents).size() != 0) {
                    return chessComponent;
                }
            }
        }
        return null;
    }

    public ChessComponent selectTarget(ChessComponent chessComponent) {
        ArrayList<ChessboardPoint> points = chessComponent.getCanMoveChess(chessComponents);
        if (level == AILevel.Random) {
            Random random = new Random();
            int i = random.nextInt(points.size());
            ChessboardPoint point = points.get(i);
            return chessComponents[point.getX()][point.getY()];
        }
        if (level == AILevel.Greedy) {
            for (ChessboardPoint point : points) {
                int X = point.getX();
                int Y = point.getY();
                if (!(chessComponents[X][Y] instanceof EmptySlotComponent)) {
                    return chessComponents[X][Y];
                }
            }
            Random random = new Random();
            int i = random.nextInt(points.size());
            ChessboardPoint point = points.get(i);
            return chessComponents[point.getX()][point.getY()];
        }
        return null;
    }

    public int chessValue (ChessComponent[][] chessComponents) {
        int sum = 0;
        for (ChessComponent[] Chess : chessComponents) {
            for (ChessComponent chess : Chess) {
                sum += hashMap.get(chess.Name());
            }
        }
        return sum;
    }
    public int chessFunction (ChessComponent[][] chessComponents, int depth, int a, int b, ChessColor color) {
        boolean move = true;
        ChessComponent[][] chessArray = new ChessComponent[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessComponent chess = chessComponents[i][j];
                chessArray[i][j] = chess.clone();
                if (chess.getChessColor() == color) {
                    ArrayList<ChessboardPoint> points = chess.getCanMoveChess(chessComponents);
                    if (points.size() != 0) {
                        move = false;
                    }
                }
            }
        }
        if (depth == 0 || move){
            return chessValue(chessComponents);
        }
        if (color == ChessColor.BLACK) {
            for (ChessComponent[] Chess : chessComponents) {
                for (ChessComponent chess : Chess) {
                    if (chess.getChessColor() == color) {
                        ArrayList<ChessboardPoint> points = chess.getCanMoveChess(chessComponents);
                        for (ChessboardPoint point: points) {
                            int sourceX = chess.getChessboardPoint().getX();
                            int sourceY = chess.getChessboardPoint().getX();
                            int targetX = point.getX();
                            int targetY = point.getY();
                            ChessComponent component1 = chessArray[sourceX][sourceY];
                            chessArray[sourceX][sourceY] = new EmptySlotComponent(component1.getChessboardPoint(),component1.getLocation(),clickController,0);
                            chessArray[targetX][targetY] = component1;
                            component1.setChessboardPoint(new ChessboardPoint(targetX, targetY));
                            int initialA = a;
                            a = Math.max(a, chessFunction(chessArray, depth - 1, a, b, ChessColor.WHITE));
                            if (depth == this.depth) {
                                if (initialA < a){
                                    this.chessboardPoint1 = chess.getChessboardPoint();
                                    this.chessboardPoint2 = point;
                                }
                            }
                            if (b <= a) break;
                        }
                    }
                }
            }
            return a;
        }
        else {
            for (ChessComponent[] Chess : chessComponents) {
                for (ChessComponent chess : Chess) {
                    if (chess.getChessColor() == color) {
                        ArrayList<ChessboardPoint> points = chess.getCanMoveChess(chessComponents);
                        for (ChessboardPoint point: points) {
                            int sourceX = chess.getChessboardPoint().getX();
                            int sourceY = chess.getChessboardPoint().getX();
                            int targetX = point.getX();
                            int targetY = point.getY();
                            ChessComponent component1 = chessArray[sourceX][sourceY];
                            chessArray[sourceX][sourceY] = new EmptySlotComponent(component1.getChessboardPoint(),component1.getLocation(),clickController,0);
                            chessArray[targetX][targetY] = component1;
                            component1.setChessboardPoint(new ChessboardPoint(targetX, targetY));

                            b = Math.min(b, chessFunction(chessArray, depth - 1, a, b, ChessColor.BLACK));
                            if (b <= a) break;
                        }
                    }
                }
            }
            return b;
        }
    }
}

