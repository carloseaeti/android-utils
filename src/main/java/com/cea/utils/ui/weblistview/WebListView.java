package com.cea.utils.ui.weblistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cea.utils.R;
import com.cea.utils.ui.inject.Inject;
import com.cea.utils.ui.inject.InjectView;

/**
 * Created by Carlos on 20/06/2014.
 */
public class WebListView extends LinearLayout implements AbsListView.OnScrollListener {

    private WebLoaderAdapter mAdapter;
    @InjectView(tag = "weblistivew_list")
    ListView mListView;
    @InjectView(tag = "weblistivew_progress")
    ProgressBar mProgressBar;

    public WebListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(LayoutInflater.from(context).inflate(R.layout.weblistview, null));
        Inject.inject(this, this);
        mListView.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisible, int visibleCount, int totalCount) {
        boolean loadMore = firstVisible + visibleCount >= totalCount;
        if(loadMore) {
            if(mAdapter != null){
                mAdapter.loadMore(this);
            }
        }
    }

    public void setAdapter(WebLoaderAdapter adapter) {
        mAdapter = adapter;
        mListView.setAdapter(adapter);
    }
}
