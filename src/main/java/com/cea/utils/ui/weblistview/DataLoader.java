package com.cea.utils.ui.weblistview;

import java.util.List;

/**
 * Created by Carlos on 20/06/2014.
 */
public abstract class DataLoader<T> {

    long elementsPerRequest;

    public DataLoader(long elementsPerRequest){
       this.elementsPerRequest = elementsPerRequest;
    }

    public abstract void onPreLoad();
    public abstract List<T> requestPage(Long actualLoader);
    public abstract void onLoadFinish(List<T> newData, List<T> currentData);
}
