package com.cea.utils.web;

import com.cea.utils.utils.IOUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * Created by thiagosilva-trix on 06/04/15.
 */
public class ThrowsServerBodyErrorResponseHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return true;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            throw new RuntimeException(IOUtils.convertStreamToString(response.getBody()));
        }
    }
}
