package view;

import javax.swing.*;
import java.util.Timer;

public class Time {
    private JLabel DaoJiShi;
    private int leftTime = 60;
    int sec;
    Timer countDown;


    public Time(JLabel daoJiShi){
        this.DaoJiShi = daoJiShi;

    }
}
