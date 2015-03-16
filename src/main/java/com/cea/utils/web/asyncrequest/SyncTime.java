package com.cea.utils.web.asyncrequest;

/**
 * Created by carlos.araujo on 19/11/2014.
 */
public class SyncTime {

    public static final long MIN_TIME = 60 * 1000;
    public static final long FIVE_MINUTES = 300000L;
    public static final long THIRTY_MINUTES = 1800000L;
    public static final long ONE_HOUR = 3600000L;
    public static final long INFINITE = -1;

    private long time = MIN_TIME;

    public SyncTime(long time){
        if(time > MIN_TIME || time == INFINITE){
            this.time = time;
        }
    }

    public long getValue() {
        return time;
    }
}
