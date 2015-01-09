package com.cea.utils.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Carlos on 02/06/2014.
 */
public class ClicableLinearLayout extends LinearLayout {

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        for(int i = 0 ; i< getChildCount() ; i++){
            getChildAt(i).setVisibility(visibility);
        }
    }

    CustomClicableView mClicableView;

    public ClicableLinearLayout(Context context) {
        super(context);
        mClicableView = new CustomClicableView(this);
    }

    public ClicableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mClicableView = new CustomClicableView(this);
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
}
