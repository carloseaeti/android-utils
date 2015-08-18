package com.cea.utils.samples.database;

import android.content.Context;

import com.cea.utils.database.DefaultRepository;

/**
 * Created by Carlos on 14/07/2015.
 */
public class Repository extends DefaultRepository {

    private static final String DATABASE_NAME = "default-database.db";
    private static final int DATABASE_VERSION = 1;

    public Repository(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public Class[] getClassList() {
        /*PUT CLASS LIST HERE*/
        return new Class[]{};
    }
}
