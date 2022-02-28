package com.wittyly.witpms.util;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;
import com.wittyly.witpms.R;
import com.wittyly.witpms.model.Project;
import com.wittyly.witpms.model.User;
import com.wittyly.witpms.ui.customviews.RoundedTransformation;

public class ImageManager {

    public static boolean drawUserImg(Context context, User user, ImageView img, int dpRadius) {

        if (user == null) {
            img.setImageDrawable(null);
            return false;
        }

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(user.getFirstname());

        String initials = "?";

        if (user.getFirstname() == null || !user.getFirstname().equals("")) {
            initials = Utilities.getInitials(user.getFirstname());
        }

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                    .width(Utilities.dpToPx(30))  // width in px
                    .height(Utilities.dpToPx(30)) // height in px
                .endConfig()
                .buildRound(initials, color);

        img.setImageDrawable(drawable);

        if (user.getImagePath() != null && !user.getImagePath().equals("")) {

            Picasso.with(context)
                    .load(Environment.WITTYLY_BASE_URL + user.getImagePath())
                    .transform(new RoundedTransformation(dpRadius, 0))
                    .placeholder(drawable)
                    .error(drawable)
                    .fit()
                    .centerCrop()
                    .into(img);

        }

        return true;

    }

    public static void drawShortname(Context context, Project project, ImageView img, int width, int height) {

        int color = ContextCompat.getColor(context, R.color.colorPrimary);

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                    .width(Utilities.dpToPx(width))  // width in px
                    .height(Utilities.dpToPx(height)) // height in px
                    .fontSize((int) Utilities.spToPx(13))
                .endConfig()
                .buildRound(project.getShortName(), color);

        img.setImageDrawable(drawable);

    }

    public static void drawShortname(Context context, String text, ImageView img, int width, int height) {

        int color = ContextCompat.getColor(context, R.color.colorPrimary);

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(Utilities.dpToPx(width))  // width in px
                .height(Utilities.dpToPx(height)) // height in px
                .fontSize((int) Utilities.spToPx(13))
                .endConfig()
                .buildRound(text, color);

        img.setImageDrawable(drawable);

    }

    public static boolean drawUserImg(Context context, User user, ImageView img, int width, int height) {

        if (user == null) {
            img.setImageDrawable(null);
            return false;
        }

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(user.getFirstname());

        String initials = "?";

        if (user.getFirstname() == null || !user.getFirstname().equals("")) {
            initials = Utilities.getInitials(user.getFirstname());
        }

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                    .width(Utilities.dpToPx(width))  // width in px
                    .height(Utilities.dpToPx(height)) // height in px
                .endConfig()
                .buildRound(initials, color);

        img.setImageDrawable(drawable);

        if (user.getImagePath() != null && !user.getImagePath().equals("")) {

            Picasso.with(context)
                    .load(Environment.WITTYLY_BASE_URL + user.getImagePath())
                    .transform(new RoundedTransformation(Utilities.dpToPx(width/2), 0))
                    .placeholder(drawable)
                    .error(drawable)
                    .fit()
                    .centerCrop()
                    .into(img);

        }

        return true;

    }

}
