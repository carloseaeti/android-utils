package com.cea.utils.web.asyncrequest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * Created by Carlos on 07/08/2015.
 */
public class AsyncClassesRepository {

    private static final String PREFERENCE_VERSION_CODE = "versionCode";
    private static final String PREFERENCE_CLASS_LIST = "classList";

    List<Class<?>> getAsyncRequestList(Context context) {
        List<String> lastAsyncRequestClassList = getLastAsyncRequestClassList(context);
        List<Class<?>> classList = new ArrayList<Class<?>>();
        try {
            if(lastAsyncRequestClassList == null){
                lastAsyncRequestClassList = new ArrayList<String>();
                createNewClassList(context, classList, lastAsyncRequestClassList);
                saveClassList(context, lastAsyncRequestClassList);
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

    private List<String> getLastAsyncRequestClassList(Context context) {
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

    private SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
    }
}
