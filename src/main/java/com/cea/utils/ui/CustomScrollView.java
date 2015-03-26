package com.cea.utils.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.internal.widget.ActionBarOverlayLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by Carlos on 23/05/2014.
 */
public class CustomScrollView extends ScrollView {

    public CustomScrollView(Context context) {
        super(context);
        fixSize();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fixSize();
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        fixSize();
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        fixSize();
    }

    private void fixSize() {
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup rootViewChild = (ViewGroup) CustomScrollView.this.getChildAt(0);
                View lastChild = rootViewChild.getChildAt(rootViewChild.getChildCount() - 1);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lastChild.getLayoutParams();
                int newMargin = calcBottomMargin(rootViewChild);
                params.topMargin = newMargin + params.topMargin;
                lastChild.setLayoutParams(params);
                CustomScrollView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    private int calcBottomMargin(ViewGroup rootView) {
        Rect statusBarSize = new Rect();
        View window = rootView.getRootView();
        window.getWindowVisibleDisplayFrame(statusBarSize);
        int methodReturn = window.getHeight() - rootView.getHeight() - statusBarSize.top;
        return  methodReturn < 0 ? 0 : methodReturn;
    }

    private int navigationBarHeight(View window) {
        Resources resources = window.getContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
