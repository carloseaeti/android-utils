package com.cea.utils.database;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import java.util.List;

/**
 * Created by carlos.araujo on 13/03/2015.
 */
public abstract class GenericDao {

    protected DefaultRepository repository;

    public GenericDao(Context context){
        repository = RepositoryFactory.getRepository(context);
    }

    public void createOrUpdate(){
        repository.createOrUpdate(this);
    }

    public void delete(){
        repository.delete(this);
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
}
