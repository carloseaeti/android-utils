package com.cea.utils.ui.weblistview;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

import com.cea.utils.R;
import com.cea.utils.web.ManagedErrorsAsyncTask;
import com.cea.utils.web.WebServiceRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 20/06/2014.
 */
public abstract class WebLoaderAdapter<T> extends BaseAdapter {

    protected DataLoader mDataLoader;
    protected List<T> mData;
    private boolean isFirstLoad = true;

    public WebLoaderAdapter(DataLoader<T> dataLoader){
        mDataLoader = dataLoader;
        mData = new ArrayList<T>();
    }

    void loadMore(WebListView listView){
        if(mDataLoader != null && !isRunning && !isDataEnd) {
            new ListAsyncLoader(listView).execute();
        }
    }

    private boolean isRunning = false;
    private boolean isDataEnd;

    private class ListAsyncLoader extends ManagedErrorsAsyncTask<Void, Void, List<T>> {

        private WebListView mListView;
        private View onLoadFooter;

        public ListAsyncLoader(WebListView listView){
            super(listView.getContext(), false);
            this.mListView = listView;
            onLoadFooter = LayoutInflater.from(listView.getContext()).inflate(R.layout.listview_load_footer, null);
        }

        @Override
        protected void onPreExecute() {
            if(isFirstLoad){
                mListView.mProgressBar.setVisibility(View.VISIBLE);
            }
            else{
                mListView.mListView.addFooterView(onLoadFooter);
            }
            mDataLoader.onPreLoad();
            isRunning = true;
        }

        @Override
        protected List<T> doInBackground(Void... voids) {
            try {
                List<T> newData = mDataLoader.requestPage((long) WebLoaderAdapter.this.getCount());
                return newData;
            }
            catch (Exception ex){
                message = WebServiceRequest.serviceRequestExceptionManager(mContext, ex, false);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<T> newData) {
            super.onPostExecute(newData);
            if(message == null){
                if(newData != null) {
                    for (T data : newData) {
                        mData.add(data);
                    }
                }
            }
            mDataLoader.onLoadFinish(newData, mData);
            if(isFirstLoad){
                mListView.mProgressBar.setVisibility(View.GONE);
            }
            else{
                mListView.mListView.removeFooterView(onLoadFooter);
            }
            isFirstLoad = false;

            notifyDataSetChanged();
            isRunning = false;
            if(newData == null || newData.size() < mDataLoader.elementsPerRequest){
                isDataEnd = true;
            }
        }
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
