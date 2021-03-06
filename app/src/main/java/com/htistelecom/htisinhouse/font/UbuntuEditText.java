package com.htistelecom.htisinhouse.font;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class UbuntuEditText extends EditText {
    public UbuntuEditText(Context context) {
        super(context);
        init(context);

    }

    public UbuntuEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UbuntuEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public UbuntuEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "regular.ttf");
        setTypeface(tf);

    }
}
