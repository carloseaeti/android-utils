package com.cea.utils.web.asyncrequest;


import com.cea.utils.web.NetworkInterface;

/**
 * Created by Carlos on 14/05/2014.
 */
public interface AsyncRequest {
    void send();
    SyncTime syncTime();
    NetworkInterface useInterface();
}
