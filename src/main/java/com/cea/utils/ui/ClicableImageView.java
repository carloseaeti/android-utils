package com.cea.utils.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class ClicableImageView extends ImageView {

    private AlphaAnimation alphaGo = new AlphaAnimation(0.5F, 0.5F);

    public ClicableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        alphaGo.setDuration(100);
        alphaGo.setFillAfter(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN && this.isClickable()){
            this.startAnimation(alphaGo);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setClickable(boolean clickable) {
        if(clickable){
            setFixAlpha(1.0F);

        }
        else{
            setFixAlpha(0.3F);
        }
        super.setClickable(clickable);
    }

    private void setFixAlpha(float v) {
        AlphaAnimation alpha = new AlphaAnimation(v,v);
        alpha.setDuration(0); // Make animation instant
        alpha.setFillAfter(true); // Tell it to persist after the animation ends
        startAnimation(alpha);
    }

}
