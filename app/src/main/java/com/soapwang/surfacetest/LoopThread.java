package com.soapwang.surfacetest;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
/**
 * Created by Acer on 2016/9/16.
 * The main game loop Thread.
 */
public class LoopThread extends Thread {
    public static final int FRAME_RATE = 60;
    private int frames = 0;
    private long interval = 0;
    private double fps = 0;
    private GameView gameView;
    private boolean running = false;

    public LoopThread(GameView view) {
        this.gameView = view;
    }

    public void setRunning(boolean run) {
        this.running = run;
    }
    public boolean getRunning() {
        return running;
    }

    public double getFrameRate() {
        return fps;
    }

    @Override
    @SuppressLint("WrongCall")
    public void run() {
        long ticksPS = 1000 / FRAME_RATE; //Wait 16ms for a frame.
        long startTime;
        long sleepTime;
        while (running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = gameView.getHolder().lockCanvas();
                synchronized (gameView.getHolder()) {
                    gameView.onDraw(c);
                }
            } finally {
                if (c != null) {
                    gameView.getHolder().unlockCanvasAndPost(c);
                }
            }
            frames++;
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
            interval += System.currentTimeMillis() - startTime;
            if(interval >= 500) {
                fps = (frames * 1000.0) / interval;
                interval = 0;
                frames = 0;
                gameView.setFps(fps);
            }
        }
    }
}
