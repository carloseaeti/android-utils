package com.cea.utils.database;

import com.j256.ormlite.dao.Dao;
import java.util.List;

/**
 * Created by carlos.araujo on 13/03/2015.
 */
public abstract class GenericDao {

    public abstract DefaultRepository getRepository();

    public void createOrUpdate(){
        DefaultRepository repository = getRepository();
        repository.createOrUpdate(this);
    }

    public void delete(){
        DefaultRepository repository = getRepository();
        repository.delete(this);
    }

    public <T> List<T> getAll(){
        DefaultRepository repository = getRepository();
        return repository.getAll(getClass());
    }

    public <T> T findById(Long id){
        DefaultRepository repository = getRepository();
        return repository.findEntityById(getClass(), id);
    }

    public long getCount() {
        DefaultRepository repository = getRepository();
        return repository.getCount(getClass());
    }

    public <T> Dao<T, ?> getDao(){
        Dao dao = getRepository().getDao(getClass());
        return dao;
    }
}
