package com.cea.utils.database;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by carlos.araujo on 11/04/2015.
 */
public abstract class RepositoryFactory {

    private static RepositoryFactory repositoryFactory;
    public abstract DefaultRepository getRepository();

    static DefaultRepository getRepository(Context context){
        if(repositoryFactory == null) {
            try {
                ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                Class repositoryClass = Class.forName(app.metaData.getString("com.cea.database.RepositoryFactory"));
                repositoryFactory = (RepositoryFactory) repositoryClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("RepositotyFactory not declared in Manifest. Use meta-data name 'com.cea.database.RepositoryFactory'");
            }
        }
        return repositoryFactory.getRepository();
    }
}
