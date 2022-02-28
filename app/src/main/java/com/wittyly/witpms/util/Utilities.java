package com.wittyly.witpms.util;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;

public class Utilities {

    public static int getStatusColor(String status) {

        switch (status) {
            case "Completed":
                return Color.parseColor("#BCBCBC");
            case "New":
                return Color.parseColor("#5CB85C");
            case "In Progress":
                return Color.parseColor("#39b3d7");
            case "Pending":
                return Color.parseColor("#DC572E");
            case "Closed":
                return Color.parseColor("#BCBCBC");
            case "Void":
                return Color.parseColor("#BF1E4B");
            case "Forwarded":
                return Color.parseColor("#BF1E4B");
            case "For Review - Staging":
                return Color.parseColor("#FF4500");
            case "For Review - Production":
                return Color.parseColor("#20B2AA");
            case "Accepted":
                return Color.parseColor("#f39c12");
            case "Reopened":
                return Color.parseColor("#D9D833");
            case "Next Up":
                return Color.parseColor("#337ab7");
            case "Ready to Publish":
                return Color.parseColor("#FF1493");
            case "Discussion Required":
                return Color.parseColor("#310DC2");
            case "For Review":
                return Color.parseColor("#9918C3");
        }

        return Color.parseColor("#FFFFFF");

    }

    public static String getStatusRGB(String status) {

        switch (status) {
            case "Completed":
                return "#BCBCBC";
            case "New":
                return "#5CB85C";
            case "In Progress":
                return "#39b3d7";
            case "Pending":
                return "#DC572E";
            case "Closed":
                return "#BCBCBC";
            case "Void":
                return "#BF1E4B";
            case "Forwarded":
                return "#BF1E4B";
            case "For Review - Staging":
                return "#FF4500";
            case "For Review - Production":
                return "#20B2AA";
            case "Accepted":
                return "#f39c12";
            case "Reopened":
                return "#D9D833";
            case "Next Up":
                return "#337ab7";
            case "Ready to Publish":
                return "#FF1493";
            case "Discussion Required":
                return "#310DC2";
            case "For Review":
                return "#9918C3";
        }

        return "#FFFFFF";

    }

    @NonNull
    public static String getInitials(String name) {

        String initials = "";

        if (name.contains(" ")) {
            initials += name.charAt(0);
            initials += name.charAt(name.indexOf(' ') + 1);
        } else if (name.length() > 1) {
            initials += name.charAt(0);
            initials += name.charAt(1);
        } else {
            initials += name.charAt(0);
        }

        return initials.toUpperCase();

    }

    public static long getNextID(Realm realm, Class c) {

        long key;

        try {
            key = (long) realm.where(c).findAll().max("id");
        } catch (NullPointerException e) {
            key = 0;
        }

        return key + 1;

    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static float spToPx(float sp) {
        return sp * (Resources.getSystem().getDisplayMetrics().scaledDensity);
    }

    public static float pxToSp(float px) {
        return px / (Resources.getSystem().getDisplayMetrics().scaledDensity);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        html = html.replaceAll("\n", "<br/>");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static String withZeros(int i) {
        if (i < 10) {
            return "00" + i;
        } else if (i < 100) {
            return "0" + i;
        } else {
            return "" + i;
        }
    }

    /*
     * Copyright 2012 Google Inc.
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *      http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getTimeAgo(Date past) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(past);

        Long time = calendar.getTimeInMillis();

        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();

        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1m ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + "m ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + "h ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + "d ago";
        }

    }

}
