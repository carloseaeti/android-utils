package com.cea.utils.ui;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * Created by Carlos on 13/06/2014.
 */
class CustomClicableView{

    private View mComponent;
    private AlphaAnimation alphaGo = new AlphaAnimation(0.5F, 0.5F);

    CustomClicableView(View component){
        this.mComponent = component;
        alphaGo.setDuration(100);
        alphaGo.setFillAfter(false);
        if(component.isClickable()){
            setFixAlpha(1.0F);
        }
        else{
            setFixAlpha(0.3F);
        }
    }

    private void setFixAlpha(float v) {
        AlphaAnimation alpha = new AlphaAnimation(v,v);
        alpha.setDuration(0);
        alpha.setFillAfter(true);
        mComponent.startAnimation(alpha);
    }

    public void onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN && mComponent.isClickable() && mComponent.isEnabled()){
            mComponent.startAnimation(alphaGo);
        }
    }

    public void setClickable(boolean clickable) {
        if(clickable){
            setFixAlpha(1.0F);

        }
        else{
            setFixAlpha(0.3F);
        }
    }
}
