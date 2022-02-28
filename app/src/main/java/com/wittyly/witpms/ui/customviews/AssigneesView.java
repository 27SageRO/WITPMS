package com.wittyly.witpms.ui.customviews;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wittyly.witpms.R;
import com.wittyly.witpms.model.User;
import com.wittyly.witpms.util.ImageManager;
import com.wittyly.witpms.util.Utilities;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class AssigneesView extends ConstraintLayout {

    private LinearLayout imageContainer;
    private TextView names;
    private List<User> assignees;
    private List<CircleImageView> images;

    private AlertDialog modal;
    private AlertDialog.Builder modalBuilder;
    private FlowLayout modalContainer;

    public AssigneesView(Context context) {
        super(context);
    }
    public AssigneesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public AssigneesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    public void init() {
        imageContainer = (LinearLayout) getChildAt(0);
        names = (TextView) getChildAt(1);
        images = new ArrayList<>();
        assignees = new ArrayList<>();
    }

    public void addAssignee(User user) {

        for (User assignee : assignees) {
            if (user.getId() == assignee.getId()) {
                return;
            }
        }

        CircleImageView temp;
        if (assignees.size() == 0) {
            temp = createImage(false, user);
            images.add(temp);
            imageContainer.addView(temp);
            names.setText(user.getFullName());
        } else if (assignees.size() == 1) {
            temp = createImage(true, user);
            images.add(temp);
            imageContainer.addView(temp);
            names.setText(assignees.get(0).getFirstname() + " & " + user.getFirstname());
        } else {
            temp = createImage(true, user);
            images.add(temp);
            imageContainer.addView(temp);
            names.setText(assignees.get(0).getFirstname() + " & " + assignees.size() + " others.");
        }

        assignees.add(user);

    }

    public void removeAssignee(User user) {

        for (int i = 0; i < assignees.size(); i++) {

            if (user == assignees.get(i)) {
                imageContainer.removeView(images.get(i));
                images.remove(i);
                assignees.remove(i);
                reArrangeImageContainer();
                if (assignees.size() == 1) {
                    names.setText(assignees.get(0).getFullName());
                } else if (assignees.size() == 2) {
                    names.setText(assignees.get(0).getFirstname() + " & " + assignees.get(1).getFirstname());
                } else {
                    names.setText(assignees.get(0).getFirstname() + " & " + (assignees.size() - 1) + " others.");
                }
                return;
            }

        }

    }

    public void reArrangeImageContainer() {
        for (int i = 0; i < imageContainer.getChildCount(); i++) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageContainer.getChildAt(i).getLayoutParams();
            if (i == 0) {
                layoutParams.leftMargin = 0;
            } else {
                layoutParams.leftMargin = -(Utilities.dpToPx(40));
            }
            imageContainer.getChildAt(i).setLayoutParams(layoutParams);
        }
    }

    public void showAssignees() {

        modalContainer = new FlowLayout(getContext());
        modalContainer.setPadding(Utilities.dpToPx(16), Utilities.dpToPx(16), Utilities.dpToPx(16), Utilities.dpToPx(16));

        for (int i = 0; i < assignees.size(); i++) {

            final int cachedIndex = i;

            final FrameLayout itemContainer = (FrameLayout) inflate(getContext(), R.layout.custom_assignees_modal_item, null);

            CircleImageView image = (CircleImageView) itemContainer.getChildAt(0);
            image.setImageDrawable(images.get(i).getDrawable());
            itemContainer.setTag(assignees.get(i).getId());

            image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (assignees.size() > 1) {
                        removeAssignee(assignees.get(cachedIndex));
                        modalContainer.removeView(itemContainer);
                    } else {
                        modal.dismiss();
                        Snackbar.make(AssigneesView.this, "No assignee is not supported.", 2000).show();
                    }
                }
            });

            modalContainer.addView(itemContainer);

        }

        modalBuilder = new AlertDialog.Builder(getContext(), R.style.AssigneeStyleDialog);
        modalBuilder.setTitle("Assignees");
        modalBuilder.setPositiveButton("OK", null);
        modalBuilder.setView(modalContainer);
        modal = modalBuilder.show();
    }

    private CircleImageView createImage(boolean insertMargin, User user) {

        CircleImageView newImage = new CircleImageView(getContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utilities.dpToPx(50), Utilities.dpToPx(50));

        if (insertMargin) {
            layoutParams.leftMargin = -(Utilities.dpToPx(40));
        }

        ImageManager.drawUserImg(getContext(), user, newImage, 50, 50);
        newImage.setLayoutParams(layoutParams);

        return newImage;

    }

    private CircleImageView cloneImage(boolean insertMargin, CircleImageView image, User user) {

        CircleImageView newImage = new CircleImageView(getContext());
        newImage.setImageDrawable(image.getDrawable());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utilities.dpToPx(50), Utilities.dpToPx(50));

        if (insertMargin) {
            layoutParams.leftMargin = -(Utilities.dpToPx(40));
        }

        ImageManager.drawUserImg(getContext(), user, newImage, 50, 50);
        newImage.setLayoutParams(layoutParams);

        return newImage;

    }

}
