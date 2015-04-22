package com.cea.utils.database;

import android.content.Context;
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
    private Class type;

    /**
     *
     * @param type
     * @param query If null, query for all.
     */
    public AutoUpdateableArrayAdapter(Context context,  int layout, Class<T> type, PreparedQuery query){
        super(context, layout);
        dao = RepositoryFactory.getRepository(context).getDao(type);
        mQuery = query;
        this.type = type;
        update();
    }

    String getIntentFilterAction(){
        return GenericDao.getContentChangeFilter(type);
    }

    @Override
    public void notifyDataSetChanged() {
        update();
        super.notifyDataSetChanged();
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
