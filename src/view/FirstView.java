package view;

import com.tedu.manager.MusicPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class FirstView extends JFrame implements ActionListener {

    private String str;


    public FirstView(){
        this.setTitle("Chess");
        this.setSize(550, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(new Color(88,47,27));

        /*ImageIcon icon=new ImageIcon("images/bac1.jpg");
        //Image im=new Image(icon);
        //将图片放入label中
        JLabel label=new JLabel(icon);
        //设置label的大小
        label.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());
        //取窗口的第二层，将label放入
        this.getLayeredPane().add(label,Integer.MIN_VALUE);
        //获取frame的顶层容器,并设置为透明
        JPanel j=(JPanel)this.getContentPane();

        //j.setOpaque(false);

        //必须设置为透明的。否则看不到图片
        //panel.setOpaque(false);*/


        ImageIcon image = new ImageIcon("images/bac1.jpg");
        Image image1 = image.getImage();
        Image image2 = image1.getScaledInstance(getWidth(),getHeight(), Image.SCALE_FAST);
        ImageIcon trueBack = new ImageIcon(image2);
        JLabel jl = new JLabel(trueBack);
        jl.setBounds(0,0, getWidth(), getHeight());

        /*ImageIcon secondImage = new ImageIcon("images/标题.png");
        Image secondImage1 = secondImage.getImage();
        Image secondImage2 = secondImage1.getScaledInstance(100,50, Image.SCALE_FAST);
        ImageIcon trueName = new ImageIcon(secondImage2);
        JLabel jl1 = new JLabel(trueName);
        jl1.setBounds(205,100, 100,50);*/

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel name = new JLabel("Chess");
        name.setSize(200,100);
        name.setLocation(205, 70);
        name.setFont(new Font("Castellar", Font.BOLD, 36));
        name.setForeground(new Color(108, 51, 4));


        JButton bt1 = createButton("Single-Player");
        JButton bt2 = createButton("Two-Player");
        JButton bt4 = createButton("Help");
        JButton bt5 = createButton("Exit");

        bt2.setBackground(new Color(213,144,89));

        bt4.setSize(149, 81);
        bt4.setBackground(new Color(213,144,89));

        bt5.setSize(149, 81);

        bt1.setLocation(99, 164);
        bt1.setActionCommand("1");
        bt1.addActionListener(this);

        bt2.setLocation(279, 164);
        bt2.setActionCommand("2");
        bt2.addActionListener(this);



        bt4.setLocation(99, 277);
        bt4.setActionCommand("4");
        bt4.addActionListener(this);

        bt5.setLocation(279, 277);
        bt5.setActionCommand("5");
        bt5.addActionListener(this);

        panel.add(bt1);
        panel.add(bt2);

        panel.add(bt4);
        panel.add(bt5);
        panel.add(name);
        panel.add(jl);
        //panel.add(jl1);


        this.getContentPane().add(panel);

    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public JButton createButton(String text){
        JButton btn = new JButton(text);
        btn.setSize(new Dimension(149, 81));
        Font font = new Font("Castellar", Font.BOLD, 13);
        btn.setFont(font);
        btn.setBackground(new Color(163,66,0));
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("1")){
            this.setVisible(false);
            new ChessGameFrame("images/bac1.jpg","C:\\Users\\86131\\Desktop\\音频素材\\gv8rh-mzyvz.wav", new Color(207,109,60));
        }else if(e.getActionCommand().equals("2")){
            this.setVisible(false);
            new ChessGameFrame("images/bac1.jpg","C:\\Users\\86131\\Desktop\\音频素材\\gv8rh-mzyvz.wav", new Color(207,109,60));
        }else if(e.getActionCommand().equals("4")){
            new Thread() {
                //重写run方法
                public void run() {
                    //构造命令
                    String cmd = "cmd.exe /c start ";

                    //构造本地文件路径或者网页URL
                    //String file = "http://www.baidu.com";
                    String file = "https://jingyan.baidu.com/article/11c17a2c775262f446e39ddc.html";

                    try {
                        //执行操作
                        Runtime.getRuntime().exec(cmd + file);
                    } catch (IOException ignore) {
                        //打印异常
                        ignore.printStackTrace();
                    }
                }
            }.start();//启动线程
        }
        else {
            System.exit(0);
        }
    }
}
