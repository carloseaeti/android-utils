package com.cea.utils.web.asyncrequest;

/**
 * Created by carlos.araujo on 19/11/2014.
 */
public enum SyncTime {

    FIVE_MINUTES, THIRTY_MINUTES, ONE_HOUR;

    private static final long MINUTE = 60*1000;

    public long getValue() {
        if(equals(FIVE_MINUTES)){
            return 5*MINUTE;
        }
        if(equals(THIRTY_MINUTES)){
            return 30*MINUTE;
        }
        return 60*MINUTE;
    }
}
