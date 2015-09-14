package com.cea.utils.web.asyncrequest;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.cea.utils.web.NetworkInterface;

import java.util.List;


/**
 * An {@link android.app.IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class SenderService extends IntentService {

    static final String ACTION_SCHEDULED_CALL = "SCHEDULED_CALL";
    public static final String ACTION_NETWORK_STATE_CHANGE = "NETWORK_STATE_CHANGE";

    public SenderService() {
        super("SenderService");
    }

    private boolean scheduledCall;
    private long nextCallTime = SyncTime.MIN_TIME;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(Scheduler.class.getSimpleName(), "Receiver a new intent");
        guaranteeSchedule(intent);
        try {
            send();
        }
        catch (Exception ex){
            Log.w(getClass().getSimpleName(), "Sync error", ex);
        }
        finally {
            Scheduler.completeWakefulIntent(intent);
        }
        Log.d(Scheduler.class.getSimpleName(), "Service end");
    }

    private void guaranteeSchedule(Intent intent) {
        if(!scheduledCall){
            scheduledCall = intent != null && ACTION_SCHEDULED_CALL.equals(intent.getAction());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(scheduledCall) {
            Scheduler.scheduleNextCall(getApplicationContext(), nextCallTime);
        }
    }

    private void send() {
        AsyncClassesRepository classesRepository = new AsyncClassesRepository();
        SharedPreferences preferences = getPreferences(getApplicationContext());
        boolean isWifiOn = isWifiOn(getApplicationContext());
        List<Class<?>> asyncRequestList = classesRepository.getAsyncRequestList(getApplicationContext());
        syncList(preferences, asyncRequestList, isWifiOn);
    }

    private void syncList(SharedPreferences preferences, List<Class<?>> asyncRequestList, boolean isWifiOn) {
        for(Class<?> asyncRequest : asyncRequestList){
            try {
                AsyncRequest asyncRequestInstance = (AsyncRequest) asyncRequest.newInstance();
                nextCallTime = Math.min(asyncRequestInstance.syncTime().getValue(), nextCallTime);
                if(timeout(preferences, asyncRequest, asyncRequestInstance, isWifiOn)){
                    Log.d(Scheduler.class.getSimpleName(), "Calling scheduler on class ".concat(asyncRequest.getSimpleName()));
                    asyncRequestInstance.send();
                    Log.d(Scheduler.class.getSimpleName(), "Sync success on class ".concat(asyncRequest.getSimpleName()));
                    preferences.edit().putLong(asyncRequest.getName(), System.currentTimeMillis()).commit();
                }
            } catch (Exception e) {
                Log.w(Scheduler.class.getSimpleName(), "Sync error", e);
            }
        }
    }

    private boolean timeout(SharedPreferences preferences, Class<?> clazz, AsyncRequest asyncRequestInstance, boolean isWifiOn) {
        long lastRequest = preferences.getLong(clazz.getName(), Long.MIN_VALUE);
        long currentTime = System.currentTimeMillis();
        SyncTime syncTime = asyncRequestInstance.syncTime();
        if(syncTime.getValue() == SyncTime.INFINITE){
            return false;
        }
        boolean timeout = (currentTime >= lastRequest + (syncTime == null ? SyncTime.DEFAULT : syncTime.getValue()));
        return timeout && ((NetworkInterface.ONLY_WIFI.equals(asyncRequestInstance.useInterface()) && isWifiOn) || (asyncRequestInstance.useInterface().equals(NetworkInterface.ALL)));
    }

    private boolean isWifiOn(Context context){
        ConnectivityManager connMgr;
        connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifi.isConnected();
    }

    private SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
    }
}
