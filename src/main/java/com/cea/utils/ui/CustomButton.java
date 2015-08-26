package com.cea.utils.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cea.utils.R;

/**
 * Created by Carlos on 02/06/2014.
 */
public class CustomButton extends LinearLayout {

    private CustomClicableView mClicableView;
    private TextView wgtTxt;

    private boolean isRounded;

    public CustomButton(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.custom_buttom,this);
        mClicableView = new CustomClicableView(this);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.custom_buttom,this);
        appyStyles(context, attrs, view);

        mClicableView = new CustomClicableView(this);
    }

    @Override
    public void setBackgroundColor(int color) {
        if(isRounded){
            setBackgroundResource(R.drawable.rounded_drawable);
            GradientDrawable shapeDrawable = (GradientDrawable)getBackground();
            if(!isInEditMode()) {
                shapeDrawable.setColor(color);
            }
        }
        else{
            super.setBackgroundColor(color);
        }
    }

    private void appyStyles(Context context, AttributeSet attrs, ViewGroup view) {
        LinearLayout wrapper = (LinearLayout) this.getChildAt(0);
        wgtTxt = (TextView) ((ViewGroup)this.getChildAt(0)).getChildAt(0);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);
        int backgroundDrawable = a.getResourceId(R.styleable.CustomButton_background_drawable, -1);
        int textColor = a.getColor(R.styleable.CustomButton_text_color, Color.WHITE);
        //int textSize = a.getDimensionPixelSize(R.styleable.CustomButton_text_size, -1);
        int customVerticalPadding = a.getDimensionPixelSize(R.styleable.CustomButton_verticalPadding, 0);
        int customHorizontalPadding = a.getDimensionPixelSize(R.styleable.CustomButton_horizontalPadding, 0);
        String textValue = a.getString(R.styleable.CustomButton_text_value);
        isRounded = a.getBoolean(R.styleable.CustomButton_is_rounded, false);
        int backgroundColor = a.getColor(R.styleable.CustomButton_background_color, -1);

        if(isRounded && backgroundColor != -1){
            setBackgroundResource(R.drawable.rounded_drawable);
            GradientDrawable shapeDrawable = (GradientDrawable)getBackground();
            if(!isInEditMode()) {
                shapeDrawable.setColor(backgroundColor);
            }
        }
        else if(backgroundColor != -1){
            setBackgroundColor(backgroundColor);
        }

        /*if(textSize != -1) {
            wgtTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }*/

        wgtTxt.setText(textValue);
        wgtTxt.setTextColor(textColor);
        if(backgroundDrawable != -1) {
            wrapper.setBackgroundResource(backgroundDrawable);
        }
        wrapper.setPadding(customHorizontalPadding, customVerticalPadding ,customHorizontalPadding, customVerticalPadding);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mClicableView.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void setClickable(boolean clickable) {
        mClicableView.setClickable(clickable);
        super.setClickable(clickable);
    }

    public void setText(String text) {
        wgtTxt.setText(text);
    }
}
