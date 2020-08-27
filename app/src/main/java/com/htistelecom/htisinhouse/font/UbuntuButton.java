package com.htistelecom.htisinhouse.font;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

@SuppressLint("AppCompatCustomView")
public class UbuntuButton extends Button {
    public UbuntuButton(Context context) {
        super(context);
        init(context);

    }

    public UbuntuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UbuntuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public UbuntuButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mediumBold.ttf");
        setTypeface(tf);

    }
}
