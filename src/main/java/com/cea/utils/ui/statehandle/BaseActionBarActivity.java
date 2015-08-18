package com.cea.utils.ui.statehandle;

import android.os.Bundle;

/**
 * Created by Carlos on 18/08/2015.
 */
public class BaseActionBarActivity extends com.cea.utils.ui.BaseActionBarActivity {
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        StateHandler.saveInstanceState(this, outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            StateHandler.restoreInstanceState(this, savedInstanceState);
        }
    }
}
