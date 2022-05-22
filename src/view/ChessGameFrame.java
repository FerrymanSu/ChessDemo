package view;

import com.tedu.manager.MusicPlayer;
import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame implements ActionListener {
   /* private final int WIDTH;
    private final int HEIGHT;
    public final int CHESSBOARD_SIZE;*/
    private GameController gameController;
    private Chessboard chessboard;

    private JLabel jl;
    private String back = "images/bac1.jpg";
    private Color BGC = new Color(207,109,60);
    private String BGM = "C:\\Users\\86131\\Desktop\\音频素材\\gv8rh-mzyvz.wav";
    public MusicPlayer musicPlayer;
    JPanel panel;
    private JLabel hintLabel = new JLabel("Turn For WHITE");



    public String getBack() {
        return back;
    }

    public Color getBGC() {
        return BGC;
    }

    public String getBGM() {
        return BGM;
    }

   /* public void setBGC(Color BGC) {
        this.BGC = BGC;
    }

    public void setBack(String background) {
        this.back = background;
    }

    public void setBGM(String BGM) {
        this.BGM = BGM;
    }*/



    public ChessGameFrame(String back, String BGM, Color BGC) {
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

        JButton bt3 = createButton("Restart");
        bt3.setActionCommand("3");
        bt3.setLocation(800,150);
        bt3.setSize(160,50);
        bt3.addActionListener(this);

        JButton bt6 = createButton("Playback");
        bt6.setSize(160,50);
        bt6.setActionCommand("6");
        bt6.setLocation(800,450);
        bt6.addActionListener(this);

        JButton bt8 = createButton("Change BGM");
        bt8.setSize(160,50);
        bt8.setActionCommand("8");
        bt8.setLocation(800, 650);
        bt8.addActionListener(this);


        panel.add(bt3);
        panel.add(bt6);
        panel.add(bt8);
        panel.add(jl);

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
    public void addHintLabel(){
        this.hintLabel.setFont(new Font("Castellar", Font.BOLD, 20));
        this.hintLabel.setSize(250,60);
        this.hintLabel.setLocation(760,70);
        this.hintLabel.setForeground(new Color(246, 231, 215));
        add(hintLabel);
    }

    private void addChessboard() {
        chessboard = new Chessboard(650,650);
        chessboard.setHintLabel(hintLabel);
        gameController = new GameController(chessboard);
        chessboard.setLocation(getWidth()/20, getWidth()/20);
        add(chessboard);
    }

    private void addLabel() {
        JLabel statusLabel = new JLabel("Sample label");
        statusLabel.setLocation(HEIGHT, HEIGHT / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Castellar", Font.BOLD, 12));
        add(statusLabel);
    }

    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.setBackground(new Color(246, 231, 215));
        button.setLocation(800, 250);
        button.setSize(160, 50);
        button.setFont(new Font("Castellar", Font.BOLD, 12));
        panel.add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            gameController.setSave(true);
        });
    }

    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setBackground(new Color(246, 231, 215));
        button.setLocation(800, 350);
        button.setSize(160, 50);
        button.setFont(new Font("Castellar", Font.BOLD, 12));
        panel.add(button);

        button.addActionListener(e -> {
            gameController.showFileOpenDialog(this);
        });
    }

    private void addBackButton() {
        JButton button = new JButton("StepBack");
        button.setBackground(new Color(246, 231, 215));
        button.setLocation(320, 700);
        button.setSize(160, 50);
        button.setFont(new Font("Castellar", Font.BOLD, 12));
        panel.add(button);

        button.addActionListener(e -> {
            gameController.stepBack();
        });
    }

    private void addForwardButton() {
        JButton button = new JButton("StepForward");
        button.setBackground(new Color(246, 231, 215));
        button.setLocation(50, 700);
        button.setSize(160, 50);
        button.setFont(new Font("Castellar", Font.BOLD, 12));
        panel.add(button);

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

    }
    public JButton createButton(String text){
        JButton btn = new JButton(text);
        btn.setSize(new Dimension(160, 50));
        Font font = new Font("Castellar", Font.BOLD, 10);
        btn.setFont(font);
        btn.setBackground(new Color(246, 231, 215));
        return btn;
    }
    public void addChangeBGI(){
        JButton button = createButton("Change BGI");
        button.setLocation(800, 550);
        button.setSize(160,50);
        panel.add(button);
        button.addActionListener(e -> {
            /*JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(false);

            int value = chooser.showOpenDialog(button);
            if(value == JFileChooser.APPROVE_OPTION){
                panel.remove(jl);
                repaint();

                String path = chooser.getSelectedFile().getAbsolutePath();

                ImageIcon image= new ImageIcon(path);
                Image image1 = image.getImage();
                Image image2 = image1.getScaledInstance(getWidth(),getHeight(), Image.SCALE_FAST);
                ImageIcon trueBack = new ImageIcon(image2);
                jl = new JLabel(trueBack);
                jl.setBounds(0,0, getWidth(), getHeight());
                panel.add(jl);
            }*/
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
            jl1.setLocation(100,100);
            jl1.setSize(190,50);
            jl1.setFont(new Font("造字工房米萌体", Font.BOLD, 30));
            jl1.setForeground(new Color(108, 51, 4));


            JButton bt1 = createButton("钢铁烈风");
            bt1.setFont(new Font("造字工房米萌体", Font.BOLD, 15));
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

                frame.setVisible(false);

            });
            bt1.setLocation(70, 170);

            JButton bt2 = createButton("碧蓝天空");
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

                frame.setVisible(false);

            });
            bt2.setLocation(70, 240);


            panel1.add(jb);
            panel1.add(bt1);
            panel1.add(bt2);
            frame.add(jl1);
            frame.add(panel1);
            frame.setVisible(true);
        });
    }
}
