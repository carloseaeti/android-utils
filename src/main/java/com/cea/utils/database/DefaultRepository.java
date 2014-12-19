package com.cea.utils.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

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

    protected <T> void createOrUpdate(Class<T> clazz, T data){
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
}
