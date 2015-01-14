package com.cea.utils.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by carlos.araujo on 14/01/2015.
 */
public class ProgressTextView extends TextView {

    private AsyncTask<Void, String, Void> progressTask;

    public ProgressTextView(Context context) {
        super(context);
    }

    public ProgressTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void start(){
        if(progressTask != null && !progressTask.isCancelled()){
            return;
        }
        progressTask = new AsyncTask<Void, String, Void>() {

            @Override
            protected void onProgressUpdate(String[] values) {
                setText(values[0]);
            }

            @Override
            protected Void doInBackground(Void[] objects) {
                try {
                    while (!isCancelled()) {
                        Thread.sleep(800);
                        String textValue = getText().toString();
                        String textSufix = textValue.substring(textValue.length() - 3);
                        if (textSufix.endsWith(".") && !textSufix.endsWith("...")) {
                            publishProgress(textValue.concat("."));

                        } else {
                            publishProgress(textValue.replaceAll("\\.", "").concat("."));
                        }
                    }
                }
                catch (Exception ex){
                    return null;
                }
                return null;
            }
        };
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB){
            progressTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else{
            progressTask.execute();
        }
    }

    public void stop(){
        try {
            if (progressTask != null && !progressTask.isCancelled()) {
                progressTask.cancel(true);
            }
        }
        catch (Exception ex){

        }
    }
}
