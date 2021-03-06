package com.cea.utils.ui;

/**
 * Created by Carlos on 31/07/2014.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import com.cea.utils.R;

public class FontFitTextView extends TextView {

    private boolean heightSet;
    int useHeight;
    int maxSize;

    public FontFitTextView(Context context) {
        super(context);
        initialise();
    }

    public FontFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
        configureAttrs(context, attrs);
    }

    private void configureAttrs(Context context, AttributeSet attrs) {
        if(isInEditMode()){
            maxSize = 30;
        }
        else {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.FontFitTextView);
            maxSize = attrsArray.getDimensionPixelOffset(R.styleable.FontFitTextView_max_size, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, metrics));
        }
    }

    private void initialise() {
        mTestPaint = new Paint();
        mTestPaint.set(this.getPaint());
        //max size defaults to the initially specified text size unless it is too small
    }

    /* Re size the font so the specified text fits in the text box
     * assuming the text box is the specified width.
     */
    private void refitText(String text, int textWidth)
    {
        if (textWidth <= 0)
            return;
        int targetWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
        float hi = 100;
        float lo = 2;
        final float threshold = 0.5f; // How close we have to be

        mTestPaint.set(this.getPaint());

        while((hi - lo) > threshold) {
            float size = (hi+lo)/2;
            mTestPaint.setTextSize(size);
            if(mTestPaint.measureText(text) >= targetWidth)
                hi = size; // too big
            else
                lo = size; // too small
        }
        // Use lo so that we undershoot rather than overshoot
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, Math.min(lo, maxSize));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int height = heightSet ? useHeight : getMeasuredHeight();
        float oldTextSize = getTextSize();
        refitText(this.getText().toString(), parentWidth);
        float newTextSize = getTextSize();
        this.setMeasuredDimension(parentWidth, (int) (height*newTextSize/oldTextSize));
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        refitText(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        /*if (w != oldw) {
            refitText(this.getText().toString(), w);
            setHeight(o);
            if (!heightSet) {
                useHeight = getMeasuredHeight();
                heightSet = true;
            }
        }*/
        onConfigurationChanged(null);
    }

    //Attributes
    private Paint mTestPaint;
}