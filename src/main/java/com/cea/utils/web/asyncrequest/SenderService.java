package com.cea.utils.web.asyncrequest;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.cea.utils.web.NetworkInterface;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;


/**
 * An {@link android.app.IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class SenderService extends IntentService {

    private static final String PREFERENCE_VERSION_CODE = "versionCode";
    private static final String PREFERENCE_CLASS_LIST = "classList";

    static final String ACTION_SCHEDULED_CALL = "SCHEDULED_CALL";
    public static final String ACTION_NETWORK_STATE_CHANGE = "NETWORK_STATE_CHANGE";

    public SenderService() {
        super("SenderService");
    }

    private boolean scheduledCall;

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
            Scheduler.scheduleNextCall(getApplicationContext());
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void send() {
        SharedPreferences preferences = getPreferences(getApplicationContext());
        boolean isWifiOn = isWifiOn(getApplicationContext());
        List<Class<?>> asyncRequestList = getAsyncRequestList();
        syncList(preferences, asyncRequestList, isWifiOn);
    }

    private void syncList(SharedPreferences preferences, List<Class<?>> asyncRequestList, boolean isWifiOn) {
        for(Class<?> asyncRequest : asyncRequestList){
            try {
                AsyncRequest asyncRequestInstance = (AsyncRequest) asyncRequest.newInstance();
                if(timeout(preferences, asyncRequest, asyncRequestInstance, isWifiOn)){
                    asyncRequestInstance.send();
                    preferences.edit().putLong(asyncRequest.getName(), System.currentTimeMillis()).commit();
                }
            } catch (Exception e) {
                Log.w(getClass().getSimpleName(), "Sync error", e);
            }
        }
    }

    private boolean timeout(SharedPreferences preferences, Class<?> clazz, AsyncRequest asyncRequestInstance, boolean isWifiOn) {
        long lastRequest = preferences.getLong(clazz.getName(), Long.MIN_VALUE);
        long currentTime = System.currentTimeMillis();
        SyncTime syncTime = asyncRequestInstance.syncTime();
        boolean timeout = currentTime >= lastRequest + (syncTime == null ? SyncTime.ONE_HOUR.getValue() : syncTime.getValue());
        return timeout && (NetworkInterface.WIFI.equals(asyncRequestInstance.useInterface()) && isWifiOn) || (asyncRequestInstance.useInterface().equals(NetworkInterface._3G));
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

    //region Create AsyncResquesClasses Region
    private List<Class<?>> getAsyncRequestList() {
        List<String> lastAsyncRequestClassList = getLastAsyncRequestClassList(getApplicationContext());
        List<Class<?>> classList = new ArrayList<Class<?>>();
        try {
            if(lastAsyncRequestClassList == null){
                lastAsyncRequestClassList = new ArrayList<String>();
                createNewClassList(getApplicationContext(), classList, lastAsyncRequestClassList);
                saveClassList(getApplicationContext(), lastAsyncRequestClassList);
            }
            else{
                for(String clazz : lastAsyncRequestClassList){
                    classList.add(Class.forName(clazz));
                }
            }
        } catch (Exception e) {
            //Developer must solve
            throw new RuntimeException(e);
        }
        return classList;
    }

    private void createNewClassList(Context context, List<Class<?>> classList, List<String> lastAsyncRequestClassList) throws IOException, ClassNotFoundException {
        DexFile df = new DexFile(context.getPackageCodePath());
        for (Enumeration<String> iterator = df.entries(); iterator.hasMoreElements();) {
            String clazz = iterator.nextElement();
            if(clazz.contains(context.getPackageName())){
                Class<?> appClass = Class.forName(clazz);
                for(Class<?> interfaces : appClass.getInterfaces()){
                    if(interfaces.isAssignableFrom(AsyncRequest.class)){
                        lastAsyncRequestClassList.add(clazz);
                        classList.add(appClass);
                        break;
                    }
                }
            }
        }
    }

    private void saveClassList(Context context, List<String> classList) {
        getPreferences(context).edit().putString(PREFERENCE_CLASS_LIST, new Gson().toJson(classList)).commit();
    }

    public List<String> getLastAsyncRequestClassList(Context context) {
        if(appVersionChange(context)){
            return null;
        }
        else{
            Gson gson = new Gson();
            String[] classArray = gson.fromJson(getPreferences(context).getString(PREFERENCE_CLASS_LIST, ""), String[].class);
            return Lists.newArrayList(classArray);
        }
    }

    private boolean appVersionChange(Context context) {
        long versionCode = getPreferences(context).getLong(PREFERENCE_VERSION_CODE, -1);
        return versionCode != getAppCurrentVersion(context);

    }

    private int getAppCurrentVersion(Context context){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;

        }
        //never throw
        catch (PackageManager.NameNotFoundException e) {
            //never throw
            return -1;
        }
    }
    //endregion
}
