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

/**
 * This class notify, by broadcast, when data change. The broadcast action is subclass name.
 * Extras: 'content': contains change type.
 * @see com.cea.utils.database.ChangeType
 */
public abstract class GenericDao<T extends GenericDao> {

    private transient DefaultRepository repository;
    private Class<T> typeParamClass;

    public GenericDao(Class<T> clazz){
        typeParamClass = clazz;
        repository = RepositoryFactory.getRepository(Application.getContext());
    }

    public abstract Long getId();

    public void createOrUpdate(){
        boolean isUpdate = this.getId() != null;
        repository.createOrUpdate(this);
        notifyDataChange(isUpdate ? ChangeType.UPDATE : ChangeType.CREATE);
    }

    public void delete(){
        repository.delete(this);
        notifyDataChange(ChangeType.DELETE);
    }

    public void deleteAll(){
        repository.deleteAll(typeParamClass);
        notifyDataChange(ChangeType.DELETE_ALL);
    }

    public static void deleteAll(Class clazz){
        DefaultRepository repository = RepositoryFactory.getRepository(Application.getContext());
        repository.deleteAll(clazz);
        notifyDataChange(clazz, ChangeType.DELETE_ALL);
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

    public List<T> getAll(){
        return repository.getAll(typeParamClass);
    }

    public static <E> List<E> getAll(Class<E> clazz){
        DefaultRepository repository = RepositoryFactory.getRepository(Application.getContext());
        return repository.getAll(clazz);
    }

    public long getCount() {
        return repository.getCount(typeParamClass);
    }

    protected Dao getDao(){
        Dao dao = repository.getDao(typeParamClass);
        return dao;
    }

    protected static <E> Dao<E, ?> getDao(Class<E> clazz){
        DefaultRepository repository = RepositoryFactory.getRepository(Application.getContext());
        return repository.getDao(clazz);
    }

    public static <E> E findById(Class<E> clazz, long id) {
        DefaultRepository repository = RepositoryFactory.getRepository(Application.getContext());
        return repository.findEntityById(clazz, id);
    }

    private void notifyDataChange(ChangeType changeType) {
        Context appContext = Application.getContext();
        Intent notifyDataChangeIntent = new Intent();
        notifyDataChangeIntent.setAction(getClass().getName());
        notifyDataChangeIntent.putExtra("content", changeType.toString());
        notifyDataChangeIntent.putExtra("id", this.getId());
        appContext.sendBroadcast(notifyDataChangeIntent);
    }

    private static void notifyDataChange(Class clazz, ChangeType changeType) {
        Context appContext = Application.getContext();
        Intent notifyDataChangeIntent = new Intent();
        notifyDataChangeIntent.setAction(clazz.getName());
        notifyDataChangeIntent.putExtra("content", changeType.toString());
        appContext.sendBroadcast(notifyDataChangeIntent);
    }

    public static String getContentChangeFilter(Class<? extends GenericDao> clazz){
        return clazz.getName();
    }

    protected DefaultRepository getRepository(){
        if(repository == null){
            repository = RepositoryFactory.getRepository(Application.getContext());
        }
        return repository;
    }
}
