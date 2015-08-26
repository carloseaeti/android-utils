package com.cea.utils.bugs;

import android.content.Context;

/**
 * Created by carlos.araujo on 08/01/2015.
 * Fix AsyncTask 'never ends' - Preload class problem. See: https://code.google.com/p/android/issues/detail?id=20915
 */
public class Application extends android.app.Application {

    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
        }
    }

    public static Context getContext(){
        if(applicationContext == null){
            throw new RuntimeException("Application Context is null. You extends lib Application?");
        }
        return applicationContext;
    }
}
