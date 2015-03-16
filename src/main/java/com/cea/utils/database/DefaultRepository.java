package com.cea.utils.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by carlos.araujo on 19/12/2014.
 */
public abstract class DefaultRepository extends OrmLiteSqliteOpenHelper {

    public DefaultRepository(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    public abstract Class[] getClassList();

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Class[] classes = getClassList();
            for(Class clazz : classes){
                TableUtils.createTable(connectionSource, clazz);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Class[] classes = getClassList();
        for(Class clazz : classes){
            try {
                TableUtils.dropTable(connectionSource, clazz, true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        onCreate(database, connectionSource);
    }

    @Override
    public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) {
        try {
            D dao = super.getDao(clazz);
            dao.executeRaw("PRAGMA foreign_keys = ON");
            return dao;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void createOrUpdate(T data){
        Class clazz = data.getClass();
        Dao<T, Object> dao = getDao(clazz);
        try {
            dao.createOrUpdate(data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T findEntityById(Class clazz, Long id) {
        Dao<T, Object> dao = getDao(clazz);
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void delete(T... data) {
        if(data.length > 0) {
            Class clazz = data[0].getClass();
            Dao<T, Object> dao = getDao(clazz);
            try {
                List<T> dataList = Arrays.asList(data);
                dao.delete(dataList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public <T> List<T> getAll(Class clazz) {
        Dao<T, Object> dao = getDao(clazz);
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Long getCount(Class clazz) {
        Dao dao = getDao(clazz);
        try {
            return dao.countOf();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
