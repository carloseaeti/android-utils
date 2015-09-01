package com.cea.utils.web.request;

import org.springframework.http.MediaType;

/**
 * Created by Carlos on 01/09/2015.
 */
public class Payload {

    public Payload(String value, MediaType contentType) {
        this.value = value;
        this.contentType = contentType;
    }

    String value;
    MediaType contentType;
}
