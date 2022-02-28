package com.wittyly.witpms.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wittyly.witpms.R;

import java.util.ArrayList;
import java.util.List;

public class PixelFlowView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String PIXEL_COLOR = "#d62980";

    private PixelFlowThread thread;
    private Drawable background;
    private Paint pixelBackground;

    private List<Rect> pixels;
    private List<Integer> pixelSpeed;
    private List<Integer> pixelLifespans;

    public PixelFlowView(Context context) {
        super(context);
        init();
    }

    public PixelFlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PixelFlowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        getHolder().addCallback(this);
        background      = ContextCompat.getDrawable(getContext(), R.drawable.background_login_image);
        pixelBackground = new Paint();
        pixels          = new ArrayList<>();
        pixelSpeed      = new ArrayList<>();
        pixelLifespans  = new ArrayList<>();
    }

    public void update() {
        if (pixels.size() < 150) {
            int x = (int) Math.ceil(Math.random() * (getWidth() - 40));
            int dy = (int) Math.ceil(Math.random() * 5);
            int life = 300 + (int) Math.ceil(Math.random() * 500);

            pixelSpeed.add(dy);
            pixelLifespans.add(life);
            pixels.add(new Rect(x, getHeight(), x + 40, getHeight() + 40));
        }
    }

    public void start() {
        thread = new PixelFlowThread(this);
        thread.setRunning(true);
        thread.start();
    }

    public void stop() {
        boolean retry = true;
        thread.setRunning(false);
        while (retry){
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e){}
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        background.setBounds(0, 0, getWidth(), getHeight());
        background.draw(canvas);

        for (int i = 0; i < pixels.size(); i++) {

            Rect pixel      = pixels.get(i);
            int speed       = pixelSpeed.get(i);
            int lifespan    = pixelLifespans.get(i);

            if (lifespan < 0) {

                // Delete in cache
                pixels.remove(i);
                pixelSpeed.remove(i);
                pixelLifespans.remove(i);

                i--;
                continue;
            }

            pixelBackground.setColor(Color.parseColor(PIXEL_COLOR));

            if (lifespan < 255) {
                pixelBackground.setAlpha(lifespan);
            }

            pixel.offsetTo(pixel.left, pixel.top - speed);
            canvas.drawRect(pixel, pixelBackground);
            pixelLifespans.set(i, lifespan - speed);

        }

        // Log.d("# Of Pixels", pixels.size() + "");

    }

}
