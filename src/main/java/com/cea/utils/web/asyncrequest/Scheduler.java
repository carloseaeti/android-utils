package com.cea.utils.web.asyncrequest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by Carlos on 14/05/2014.
 */
public class Scheduler extends WakefulBroadcastReceiver {

    private static final String PREFERENCE_LAST_SYNC = "lastSync";
    private static final String TAG = Scheduler.class.getSimpleName();
    public static final String ACTION_SCHEDULER_CALL = "com.cea.utils.web.asyncrequest.scheduler.action.START";

    @Override
    public void onReceive(Context context, Intent intent) {
        validadeAction(intent);
        Log.d(TAG, "Receive new request");
        if (scheduledCall(intent) || minOffsetNetworkCallTimeout(getPreferences(context))) {
            Intent service = new Intent(context, SenderService.class);
            service.setAction(createAction(intent));
            Log.d(TAG, "StartService");
            startWakefulService(context, service);
        }
    }

    private void validadeAction(Intent intent) {
        if(intent.getAction() == null){
            throw new RuntimeException("Null Action not supported");
        }
    }

    private boolean scheduledCall(Intent intent) {
        return ACTION_SCHEDULER_CALL.equals(intent.getAction());
    }

    private String createAction(Intent intent) {
        if(intent != null && ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            return SenderService.ACTION_NETWORK_STATE_CHANGE;
        }
        return SenderService.ACTION_SCHEDULED_CALL;
    }

    private boolean minOffsetNetworkCallTimeout(SharedPreferences preferences) {
        long lastSync = preferences.getLong(PREFERENCE_LAST_SYNC, Long.MIN_VALUE);
        boolean timeout = System.currentTimeMillis() >=  lastSync + SyncTime.FIVE_MINUTES.getValue() / 5;
        if(timeout){
            preferences.edit().putLong(PREFERENCE_LAST_SYNC, System.currentTimeMillis()).commit();
        }
        return timeout;
    }

    private SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
    }

    static void scheduleNextCall(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ACTION_SCHEDULER_CALL);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + SyncTime.FIVE_MINUTES.getValue() + 10 * 1000,
                alarmIntent);
    }
}
