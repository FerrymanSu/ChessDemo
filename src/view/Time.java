package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Time {
    private JLabel DaoJiShi;
    private int leftTime = 60;
    Timer countDown;
    private Chessboard chessboard;


    public Time(JLabel daoJiShi, Chessboard chessboard){
        this.DaoJiShi = daoJiShi;
        this.chessboard = chessboard;
        countDown = new Timer(1000, new countDownListener());
    }
    public void reset(){
        leftTime = 60;
    }
    public void start(){
        countDown.start();
    }
    class countDownListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(leftTime > 0){
                DaoJiShi.setText("TimeLeft: " + leftTime + " s");
                leftTime--;
            }else {
                DaoJiShi.setText("Time's Up!");
                reset();
                start();
                chessboard.swapColor();
            }
        }
    }
}
