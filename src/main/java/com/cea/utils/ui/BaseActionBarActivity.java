package com.cea.utils.ui;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Carlos on 15/07/2015.
 */
public class BaseActionBarActivity extends ActionBarActivity {
    protected View getView(){
        return ((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0);
    }
}
