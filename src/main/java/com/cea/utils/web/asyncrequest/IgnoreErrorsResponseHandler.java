package com.cea.utils.web.asyncrequest;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * Created by carlos.araujo on 05/02/2015.
 */
public class IgnoreErrorsResponseHandler implements ResponseErrorHandler{

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

    }
}
