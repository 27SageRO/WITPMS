package com.wittyly.witpms.ui.customviews;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;

import com.wittyly.witpms.R;
import com.wittyly.witpms.util.Utilities;

public class FabCombo extends CoordinatorLayout implements View.OnClickListener {

    private View overlay;

    private FloatingActionButton createTicket;
    private FloatingActionButton createTask;
    private FloatingActionButton createOptions;

    private Button createTicketInfo;
    private Button createTaskInfo;

    // Default distance where fab hid behind the plus fab
    private static int y = 24;

    // The distance to animate
    private static float dy1 = 84.0F;
    private static float dy2 = 144.0F;

    private CreateButtonListener listener;

    public FabCombo(Context context) {
        super(context);
    }

    public FabCombo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FabCombo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(CreateButtonListener listener) {
        this.listener = listener;
    }

    private void moveFabVertically(FloatingActionButton fab, float dy) {
        ViewCompat.animate(fab)
                .translationY(-dy)
                .withLayer()
                .setDuration(200L)
                .setInterpolator(new OvershootInterpolator(1.0F))
                .start();
    }

    private void rotateFabForward(final FloatingActionButton fab) {

        ViewCompat.animate(fab)
                .rotation(135.0F)
                .withLayer()
                .setDuration(300L)
                .setInterpolator(new OvershootInterpolator(2.0F))
                .setListener(new ViewPropertyAnimatorListener() {

                    @Override
                    public void onAnimationStart(View view) {
                        fab.setOnClickListener(null);

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        createTaskInfo.setVisibility(VISIBLE);
                        createTicketInfo.setVisibility(VISIBLE);
                        fab.setOnClickListener(FabCombo.this);
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }

                })
                .start();

        overlay.setVisibility(VISIBLE);

    }


    private void rotateFabBackward(final FloatingActionButton fab) {

        ViewCompat.animate(fab)
                .rotation(0.0F)
                .withLayer()
                .setDuration(300L)
                .setInterpolator(new OvershootInterpolator(2.0F))
                .setListener(new ViewPropertyAnimatorListener() {

                    @Override
                    public void onAnimationStart(View view) {
                        fab.setOnClickListener(null);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        fab.setOnClickListener(FabCombo.this);
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }

                })
                .start();

        createTaskInfo.setVisibility(GONE);
        createTicketInfo.setVisibility(GONE);
        overlay.setVisibility(GONE);

    }

    private void init() {

        overlay = getChildAt(0);

        createOptions = (FloatingActionButton) getChildAt(5);
        createTask = (FloatingActionButton) getChildAt(2);
        createTicket = (FloatingActionButton) getChildAt(4);

        createTaskInfo = (Button) getChildAt(1);
        createTicketInfo = (Button) getChildAt(3);

        createOptions.setImageDrawable(
                VectorDrawableCompat.create(
                        getResources(),
                        R.drawable.ic_plus_light,
                        null
                )
        );

        createTask.setImageDrawable(
                VectorDrawableCompat.create(
                        getResources(),
                        R.drawable.ic_task,
                        null
                )
        );

        createTicket.setImageDrawable(
                VectorDrawableCompat.create(
                        getResources(),
                        R.drawable.ic_ticket,
                        null
                )
        );

        createOptions.setOnClickListener(this);
        createTask.setOnClickListener(this);
        createTicket.setOnClickListener(this);
        overlay.setOnClickListener(this);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_ticket:
                if (listener != null) {
                    listener.createTicket();
                }
                break;
            case R.id.create_task:
                if (listener != null) {
                    listener.createTask();
                }
                break;
            case R.id.create_options:
                if (createOptions.getRotation() > 0) {
                    rotateFabBackward(createOptions);
                    moveFabVertically(createTicket, y);
                    moveFabVertically(createTask, y);
                } else {
                    rotateFabForward(createOptions);
                    moveFabVertically(createTicket, Utilities.dpToPx((int) dy1));
                    moveFabVertically(createTask, Utilities.dpToPx((int) dy2));
                }
                break;
        }
    }

    public interface CreateButtonListener {
        void createTask();
        void createTicket();
    }

}
