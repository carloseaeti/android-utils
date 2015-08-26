package com.cea.utils.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cea.utils.R;

/**
 * Created by Carlos on 17/08/2015.
 */
public class Divider extends LinearLayout {
    public Divider(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.Divider);
        if(!isInEditMode()) {
            addView(LayoutInflater.from(context).inflate(R.layout.divider, null));
        }
        TextView wgtTxt = (TextView) findViewById(R.id.divider_text);
        CharSequence mText = attrsArray.getText(R.styleable.Divider_text);
        if(mText != null) {
            wgtTxt.setText(mText);
        }
    }
}
