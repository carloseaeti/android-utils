package com.cea.utils.bugs;

/**
 * Created by carlos.araujo on 08/01/2015.
 * Fix AsyncTask 'never ends' problem. See: https://code.google.com/p/android/issues/detail?id=20915
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
        }
    }
}
