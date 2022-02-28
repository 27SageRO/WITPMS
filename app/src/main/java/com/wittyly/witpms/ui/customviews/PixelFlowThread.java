package com.wittyly.witpms.ui.customviews;


import android.annotation.SuppressLint;
import android.graphics.Canvas;

public class PixelFlowThread extends Thread {
    static final long FPS = 15;
    private PixelFlowView view;
    private boolean running = false;

    public PixelFlowThread(PixelFlowView view) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    @SuppressLint("WrongCall")
    public void run(){
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while(running){
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try{
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()){
                    view.onDraw(c);
                    view.update();
                }
            } finally {
                if (c != null){
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            }catch (Exception e){}
        }
    }
}
