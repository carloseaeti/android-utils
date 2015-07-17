package com.cea.utils.database;

import android.content.Context;
import android.content.Intent;

import com.cea.utils.bugs.Application;
import com.j256.ormlite.dao.Dao;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by carlos.araujo on 13/03/2015.
 */
public abstract class GenericDao {

    protected DefaultRepository repository;

    public GenericDao(){
        repository = RepositoryFactory.getRepository(Application.getContext());
    }

    public void createOrUpdate(){
        boolean isUpdate = this.getId() != null;
        repository.createOrUpdate(this);
        notifyDataChange(isUpdate ? ChangeType.UPDATE : ChangeType.CREATE);
    }

    private void notifyDataChange(ChangeType changeType) {
        Context appContext = Application.getContext();
        Intent notifyDataChangeIntent = new Intent();
        notifyDataChangeIntent.setAction(getClass().getName());
        notifyDataChangeIntent.putExtra("content", changeType.toString());
        notifyDataChangeIntent.putExtra("id", this.getId());
        appContext.sendBroadcast(notifyDataChangeIntent);
    }

    public void delete(){
        repository.delete(this);
        notifyDataChange(ChangeType.DELETE);
    }

    public void deleteAll(){
        repository.deleteAll(this.getClass());
        notifyDataChange(ChangeType.DELETE);
    }

    public <T extends GenericDao> void createOrUpdate(final T... data){
        if(data.length > 0){
            Dao dao = getDao();
            try {
                dao.callBatchTasks(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        for(T obj : data){
                            obj.createOrUpdate();
                        }
                        return null;
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public <T> List<T> getAll(){
        return repository.getAll(getClass());
    }

    public <T> T findById(Long id){
        return repository.findEntityById(getClass(), id);
    }

    public long getCount() {
        return repository.getCount(getClass());
    }

    public <T> Dao<T, ?> getDao(){
        Dao dao = repository.getDao(getClass());
        return dao;
    }

    public abstract Long getId();

    public static String getContentChangeFilter(Class<? extends GenericDao> clazz){
        return clazz.getName();
    }
}
