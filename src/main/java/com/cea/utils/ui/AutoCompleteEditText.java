package com.cea.utils.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;

/**
 * Created by carlos.araujo on 09/04/2015.
 */
public class AutoCompleteEditText extends AutoCompleteTextView {

    private Object selectedItem;

    public AutoCompleteEditText(Context context) {
        super(context);
        init();
    }

    public AutoCompleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoCompleteEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = getAdapter().getItem(i);
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDropDown();
            }
        });
    }

    private Object matchWithToStringItem(String itemStr, ListAdapter list) {
        for (int i = 0; i < list.getCount(); i++) {
            Object item = list.getItem(i);
            if (itemStr.equals(item.toString())) {
                return item;
            }
        }
        return null;
    }

    public Object getSelectedItem() {
        Object matchObject = matchWithToStringItem(getText().toString(), getAdapter());
        if (matchObject == null) {
            return null;
        } else if (matchObject.toString().equals(selectedItem == null ? null : selectedItem.toString())) {
            return selectedItem;
        }
        return matchObject;
    }

    public void setSelection(Object obj) {
        ListAdapter adapter = getAdapter();
        if(adapter != null && obj != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                Object item = adapter.getItem(i);
                if (item.equals(obj)) {
                    selectedItem = item;
                    setText(item.toString());
                    break;
                }
            }
        }
    }
}