package com.cea.utils.web.request;

import org.apache.http.StatusLine;

/**
 * Created by Carlos on 21/08/2015.
 */
public class ServerException extends RuntimeException {

    private int statusCode;

    public ServerException(StatusLine statusLine) {
        super(statusLine.getReasonPhrase());
        statusCode = statusLine.getStatusCode();
    }

    public int getStatusCode(){
        return statusCode;
    }
}
