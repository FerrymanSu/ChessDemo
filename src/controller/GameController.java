package controller;

import view.Chessboard;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameController {
    private final Chessboard chessboard;
    String date;
    private boolean save;

    public void setSave(boolean save) {
        this.save = save;
    }

    public GameController(Chessboard chessboard) {
        this.chessboard = chessboard;
        this.chessboard.setGameController(this);
        this.save = false;
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH_mm_ss");
        this.date = df.format(new Date()).trim();
        File file = new File(".\\data\\"+date);
        boolean flag = file.mkdir();
        System.out.println(flag ? "Successful!":"failed!");
    }

    public void loadGameFromFile(File file) {
        String Path = file.getPath();
        String str = Path.substring(Path.length() - 3);
        if (str.equals("txt")){
            BufferedReader reader = null;
            List<String> chessData = new ArrayList<>();
            try {
                FileInputStream fileInputStream = new FileInputStream(Path);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                reader = new BufferedReader(inputStreamReader);
                String tempString;
                while ((tempString = reader.readLine()) != null){
                    chessData.add(tempString);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            chessboard.loadGame(chessData);
        }
        else {
            JOptionPane.showMessageDialog(null,"文件格式错误！","文件读取异常",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void saveGameToFile(List<String> chessData) {
        BufferedWriter writer = null;
        File file = new File(".\\data\\"+ date +"\\" + chessboard.getRound() + ".txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false), StandardCharsets.UTF_8));
            for (String data : chessData){
                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("文件写入成功！");
    }

    public void stepBack() {
        File file = new File(".\\data\\"+ date + "\\" + (chessboard.getRound() - 1) + ".txt");
        if (file.exists()) {
            loadGameFromFile(file);
            chessboard.setRound(chessboard.getRound() - 1);
        }
    }

    public void stepForward() {
        File file = new File(".\\data\\"+ date + "\\" + (chessboard.getRound() + 1) + ".txt");
        if (file.exists()) {
            loadGameFromFile(file);
            chessboard.setRound(chessboard.getRound() + 1);
        }
    }

    public void deleteFileNotSaved(){
        if (!save) {
            String dir = ".\\data\\"+ date;
            File dirFile = new File(dir);
            if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
                System.out.println("删除文件夹失败：" + dir + "不存在！");
                return;
            }
            boolean flag = true;
            File[] files = dirFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        flag = file.delete();
                        if (!flag)
                            break;
                    }
                    else if (file.isDirectory()) {
                        flag = file.delete();
                        if (!flag)
                            break;
                    }
                }
            }
            if (!flag) {
                System.out.println("删除文件夹失败！");
            }
            if (dirFile.delete()) {
                System.out.println("删除文件夹" + dir + "成功！");
            }
        }
    }

    /*
     * 打开文件
     */
    public void showFileOpenDialog(Component parent) {
        // 创建一个默认的文件选取器
        JFileChooser fileChooser = new JFileChooser();

        // 设置默认显示的文件夹为当前文件夹
        fileChooser.setCurrentDirectory(new File(".\\data"));

        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        // 设置是否允许多选
        fileChooser.setMultiSelectionEnabled(false);

        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"确定", 则获取选择的文件路径
            File file = fileChooser.getSelectedFile();
            this.loadGameFromFile(file);

        }
    }

    /*
     * 选择文件保存路径
     */
    private static void showFileSaveDialog(Component parent) {
        // 创建一个默认的文件选取器
        JFileChooser fileChooser = new JFileChooser();

        // 设置打开文件选择框后默认输入的文件名
        fileChooser.setSelectedFile(new File(".\\data\\棋局.txt"));

        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showSaveDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"保存", 则获取选择的保存路径
            File file = fileChooser.getSelectedFile();

        }
    }
}
