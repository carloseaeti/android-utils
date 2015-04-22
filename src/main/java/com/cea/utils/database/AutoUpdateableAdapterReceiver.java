package com.cea.utils.database;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by thiagosilva-trix on 17/04/15.
 */
public class AutoUpdateableAdapterReceiver extends BroadcastReceiver {

    private AutoUpdateableArrayAdapter adapter;

    public AutoUpdateableAdapterReceiver(AutoUpdateableArrayAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        adapter.notifyDataSetChanged();
    }

    public IntentFilter getIntentFilter(){
        return  new IntentFilter(adapter.getIntentFilterAction());
    }

}
