package com.mytaxi.app.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.mytaxi.app.R;

public class EditTextMedium extends android.support.v7.widget.AppCompatEditText {

    public EditTextMedium(Context context) {
        super(context);
        init(context);
    }

    public EditTextMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditTextMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.roboto_medium));
        setTypeface(typeface);
    }
}
