package com.cea.utils.database;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ArrayAdapter;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.util.List;

/**
 * Created by carlos.araujo on 15/04/2015.
 */
public class AutoUpdateableArrayAdapter<T extends GenericDao> extends ArrayAdapter<T> {

    private List<T> data;
    private Dao<T, Object> dao;
    private PreparedQuery mQuery;
    private BroadcastReceiver mReceiver;

    /**
     *
     * @param type
     * @param query If null, query for all.
     */
    public AutoUpdateableArrayAdapter(Context context, int layout, Class<T> type, PreparedQuery query){
        super(context, layout);
        dao = RepositoryFactory.getRepository(context).getDao(type);
        mQuery = query;
        update();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                update();
                notifyDataSetChanged();
            }
        };
        context.registerReceiver(mReceiver, new IntentFilter(GenericDao.getContentChangeFilter(type)));
    }

    private void update() {
        try {
            if(mQuery == null){
                data = dao.queryForAll();
            }
            else {
                data = dao.query(mQuery);
            }
        }
        catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try{
            getContext().unregisterReceiver(mReceiver);
        }
        catch (Exception ex){}
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }
}
