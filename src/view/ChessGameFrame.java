package view;

import com.tedu.manager.MusicPlayer;
import controller.GameController;
import model.ChessColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame implements ActionListener {
    private GameController gameController;
    private Chessboard chessboard;

    private JLabel jl;
    private String back;
    private String BGM;
    public MusicPlayer musicPlayer;
    JPanel panel;
    private JLabel hintLabel = new JLabel("Turn For WHITE");
    private JLabel daoJiShi = new JLabel("TimeLeft: 60 s");



    public String getBack() {
        return back;
    }

    public String getBGM() {
        return BGM;
    }

    public ChessGameFrame(String back, String BGM) {
        setTitle("2022 CS102A Project Demo"); //设置标题
        this.back = back;
        this.BGC = BGC;
        this.BGM = BGM;

        musicPlayer = new MusicPlayer(getBGM());
        musicPlayer.play();
        musicPlayer.setLoop(true);

        this.setTitle("ChessGame");
        this.setSize(1000,800);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon image = new ImageIcon(getBack());
        Image image1 = image.getImage();
        Image image2 = image1.getScaledInstance(getWidth(),getHeight(), Image.SCALE_FAST);
        ImageIcon trueBack = new ImageIcon(image2);
        jl = new JLabel(trueBack);
        jl.setBounds(0,0, getWidth(), getHeight());

        panel = new JPanel();
        panel.setLayout(null);





        panel.add(jl);
        addDaoJiShi();
        addRestartButton();
        addChessboard();
        addHintLabel();
        addChangeBGI();
        addSaveButton();
        addLoadButton();
        addBackButton();
        addForwardButton();
        doShutDownWork();

        this.add(panel);
        this.setVisible(true);


    }
    public void addDaoJiShi(){
        this.daoJiShi.setFont(new Font("Castellar", Font.BOLD, 23));
        this.daoJiShi.setSize(250, 60);
        this.daoJiShi.setLocation(760,170);
        this.daoJiShi.setForeground(new Color(246, 231, 215));
        Time time = new Time(daoJiShi, chessboard);
        time.start();
        add(daoJiShi);
    }

    public void addHintLabel(){
        this.hintLabel.setFont(new Font("Castellar", Font.BOLD, 21));
        this.hintLabel.setSize(250,60);
        this.hintLabel.setLocation(760,70);
        this.hintLabel.setForeground(new Color(246, 231, 215));
        add(hintLabel);
    }

    private void addChessboard() {
        chessboard = new Chessboard(650,650);
        chessboard.setHintLabel(hintLabel);
        gameController = new GameController(chessboard);
        chessboard.setLocation(getWidth()/20, getHeight()/20);
        add(chessboard);
    }
    public void addRestartButton(){
        JButton bt3 = createButton("Restart");
        bt3.setActionCommand("3");
        bt3.setLocation(800,320);
        bt3.setSize(160,50);
        add(bt3);

        bt3.addActionListener(e->{

        });
    }


    private void addSaveButton() {
        JButton button = createButton("Save");
        button.setLocation(800, 420);
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            gameController.setSave(true);
        });
    }

    private void addLoadButton() {
        JButton button = createButton("Load");
        button.setLocation(800, 520);
        add(button);

        button.addActionListener(e -> {
            gameController.showFileOpenDialog(this);
        });
    }

    private void addBackButton() {
        JButton button = new JButton("StepBack");
        button.setBackground(new Color(246, 231, 215));
        button.setLocation(540, 700);
        button.setSize(160, 50);
        button.setForeground(new Color(138, 93, 47));
        button.setFont(new Font("Castellar", Font.BOLD, 14));
        add(button);

        button.addActionListener(e -> {
            gameController.stepBack();
        });
    }

    private void addForwardButton() {
        JButton button = new JButton("StepForward");
        button.setBackground(new Color(246, 231, 215));
        button.setLocation(50, 700);
        button.setSize(160, 50);
        button.setForeground(new Color(138, 93, 47));
        button.setFont(new Font("Castellar", Font.BOLD, 14));
        add(button);

        button.addActionListener(e -> {
            gameController.stepForward();
        });
    }


    private void doShutDownWork(){
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                gameController.deleteFileNotSaved();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("3")) {
            gameController.loadGameFromFile(new File(".\\data\\Initial.txt"));
        }
    }

    public JButton createButton(String text){
        JButton btn = new JButton(text);
        btn.setSize(new Dimension(160, 50));
        Font font = new Font("Castellar", Font.BOLD, 14);
        btn.setForeground(new Color(138, 93, 47));
        btn.setFont(font);
        btn.setBackground(new Color(246, 231, 215));
        return btn;
    }
    public void addChangeBGI(){
        JButton button = createButton("Change BGI");
        button.setLocation(800, 620);
        button.setSize(160,50);
        add(button);
        button.addActionListener(e -> {
            JFrame frame = new JFrame("Change BGI");
            frame.setLocation(getWidth()/2, getHeight()/2);
            frame.setSize(300,400);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel1 = new JPanel();
            panel1.setLayout(null);

            ImageIcon pic = new ImageIcon("images/bac1.jpg");
            Image pic1 = pic.getImage();
            Image pic2 = pic1.getScaledInstance(300,400, Image.SCALE_FAST);
            ImageIcon truePic = new ImageIcon(pic2);
            JLabel jb = new JLabel(truePic);
            jb.setBounds(0,0, frame.getWidth(), frame.getHeight());

            JLabel jl1 = new JLabel("切换背景");
            jl1.setLocation(100,50);
            jl1.setSize(190,50);
            jl1.setFont(new Font("造字工房米萌体", Font.BOLD, 30));
            jl1.setForeground(new Color(108, 51, 4));


            JButton bt1 = createButton("钢铁烈风");
            bt1.setFont(new Font("造字工房米萌体", Font.BOLD, 15));
            bt1.setForeground(new Color(108,51,4));
            bt1.setBackground(new Color(207,109,60));
            bt1.addActionListener(e1 -> {
                panel.remove(jl);
                repaint();

                ImageIcon picture= new ImageIcon("C:\\Users\\86131\\Desktop\\proj素材\\back3.jpg");
                Image picture1 = picture.getImage();
                Image picture2 = picture1.getScaledInstance(getWidth(),getHeight(), Image.SCALE_FAST);
                ImageIcon trueP = new ImageIcon(picture2);
                jl = new JLabel(trueP);
                jl.setBounds(0,0, getWidth(), getHeight());
                panel.add(jl);

                chessboard.change(new Color(232,232,232), new Color(102,102,102));

                frame.setVisible(false);

            });
            bt1.setLocation(70, 120);

            JButton bt2 = createButton("碧蓝天空");
            bt2.setForeground(new Color(108,51,4));
            bt2.setFont(new Font("造字工房米萌体", Font.BOLD, 15));
            bt2.setBackground(new Color(207,109,60));
            bt2.addActionListener(e1 -> {
                panel.remove(jl);
                repaint();

                ImageIcon picture= new ImageIcon("C:\\Users\\86131\\Desktop\\proj素材\\back2.jpg");
                Image picture1 = picture.getImage();
                Image picture2 = picture1.getScaledInstance(getWidth(),getHeight(), Image.SCALE_FAST);
                ImageIcon trueP = new ImageIcon(picture2);
                jl = new JLabel(trueP);
                jl.setBounds(0,0, getWidth(), getHeight());
                panel.add(jl);

                chessboard.change(new Color(255,255,255), new Color(32,96,184));

                frame.setVisible(false);

            });
            bt2.setLocation(70, 190);

            JButton bt3 = createButton("原背景");
            bt3.setForeground(new Color(108,51,4));
            bt3.setFont(new Font("造字工房米萌体", Font.BOLD, 15));
            bt3.setBackground(new Color(207,109,60));
            bt3.addActionListener(e1->{
                panel.remove(jl);
                repaint();

                ImageIcon picture= new ImageIcon("images/bac1.jpg");
                Image picture1 = picture.getImage();
                Image picture2 = picture1.getScaledInstance(getWidth(),getHeight(), Image.SCALE_FAST);
                ImageIcon trueP = new ImageIcon(picture2);
                jl = new JLabel(trueP);
                jl.setBounds(0,0, getWidth(), getHeight());
                panel.add(jl);

                chessboard.change(new Color(103,67,49), new Color(231,203,167));

                frame.setVisible(false);
            });
            bt3.setLocation(70,270);


            panel1.add(jb);
            frame.add(bt1);
            frame.add(bt2);
            frame.add(bt3);
            frame.add(jl1);
            frame.add(panel1);
            frame.setVisible(true);
        });
    }
}
