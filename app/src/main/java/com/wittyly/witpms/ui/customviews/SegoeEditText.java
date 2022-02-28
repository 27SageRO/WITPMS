package com.wittyly.witpms.ui.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.wittyly.witpms.util.FontCache;

public class SegoeEditText extends AppCompatEditText {

    public SegoeEditText(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public SegoeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public SegoeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("SourceSansPro-Regular.ttf", context);
        setTypeface(customFont);
    }

    public String getStringText() {
        return this.getText().toString();
    }

}
