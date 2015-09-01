package com.cea.utils.web.request;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Carlos on 20/08/2015.
 */
public class HttpClient {

    private String defaultMessageError;

    public HttpClient(){
        client = getThreadSafeClient();
    }

    private DefaultHttpClient client;

    public String executeStringResponse(Context context, String urlRequest, HttpMethod method, List<NameValuePair> params, Payload payload){
        try{
            HttpResponse response = execute(urlRequest, method, params, payload);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(NetworkExceptionHandler.handle(context, e, true, defaultMessageError));
        }
    }

    public <T> T executeJSONResponse(Context context, String urlRequest, HttpMethod method, List<NameValuePair> params, Payload payload, Class<T> clazz){
        try {
            HttpResponse response = execute(urlRequest, method, params, payload);
            ObjectMapper jsonConverter = new ObjectMapper();
            return jsonConverter.readValue(response.getEntity().getContent(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(NetworkExceptionHandler.handle(context, e, true, defaultMessageError));
        }
    }

    public void setDefaultMessageError(String defaultMessageError){
        this.defaultMessageError = defaultMessageError;
     }

    public HttpResponse execute(String urlRequest, HttpMethod method, List<NameValuePair> params, Payload payload){
        HttpUriRequest request = null;
        if (method == HttpMethod.POST) {
            request = buildPost(urlRequest, params, payload == null ? null : payload.value);
        } else if (method == HttpMethod.GET) {
            String queryParams = params != null ? "?" + URLEncodedUtils.format(params, "utf-8") : "";
            request = new HttpGet(urlRequest + queryParams);
        }
        if(payload != null && payload.contentType != null) {
            request.addHeader("Content-Type", payload.contentType.toString());
        }
        else{
            request.addHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        }
        return handle(client, request);
    }

    //region Aux Methods
    private DefaultHttpClient getThreadSafeClient()  {
        DefaultHttpClient client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        HttpParams params = client.getParams();
        client = new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), params);
        return client;
    }

    private HttpUriRequest buildPost(String urlRequest, List<NameValuePair> params, String payload) {
        HttpPost postRequest = new HttpPost(urlRequest);
        try {
            if(payload != null){
                postRequest.setEntity(new ByteArrayEntity(payload.getBytes("utf-8")));
            }
            //TODO improvement - add params AND payload
            else if(params != null) {
                postRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return postRequest;
    }



    public static HttpResponse handle(org.apache.http.client.HttpClient client, HttpUriRequest request){
        try {
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
                Log.w("HttpClient Server Error", EntityUtils.toString(response.getEntity()));
                throw new ServerException(response.getStatusLine());
            }
            return response;
        } catch (IOException e) {
            throw new UnknownHttpException(e);
        }
    }
    //endregion
}
