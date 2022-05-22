package com.tedu.manager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

public class MusicPlayer {
    private final File file;
    private volatile boolean isLoop = false;
    private volatile boolean isPlaying;

    private PlayThread playThread;


    public MusicPlayer(String srcPath) {
        file = new File(srcPath);
    }

    public void play() {
        if (playThread == null) {
            playThread = new PlayThread();
            playThread.start();
        }
    }

    public void over() {
        isPlaying = false;
        if (playThread != null) {
            playThread = null;
        }
    }

    public MusicPlayer setLoop(boolean isLoop) {
        this.isLoop = isLoop;
        return this;
    }

    private class PlayThread extends Thread {

        @Override
        public void run() {
            isPlaying = true;
            do {
                SourceDataLine sourceDataLine = null;
                BufferedInputStream bufIn = null;
                AudioInputStream audioIn = null;
                try {
                    bufIn = new BufferedInputStream(new FileInputStream(file));
                    audioIn = AudioSystem.getAudioInputStream(bufIn); // 可直接传入file

                    AudioFormat format = audioIn.getFormat();
                    sourceDataLine = AudioSystem.getSourceDataLine(format);
                    sourceDataLine.open();
                    // 必须open之后
                    // FloatControl.Type.MASTER_GAIN的值(可用于调节音量)
                    float newVolume = 7;
                    if (newVolume != 7) {
                        FloatControl control = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
                        control.setValue(newVolume);
                    }

                    sourceDataLine.start();
                    byte[] buf = new byte[512];
//					System.out.println(audioIn.available());
                    int len = -1;
                    while (isPlaying && (len = audioIn.read(buf)) != -1) {
                        sourceDataLine.write(buf, 0, len);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (sourceDataLine != null) {
                        sourceDataLine.drain();
                        sourceDataLine.close();
                    }
                    try {
                        if (bufIn != null) {
                            bufIn.close();
                        }
                        if (audioIn != null) {
                            audioIn.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } while (isPlaying && isLoop);
        }
    }
}