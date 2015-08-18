package com.cea.utils.web;

import android.content.Context;
import android.util.Log;

import com.cea.utils.R;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Carlos on 14/05/2014.
 */
public class WebServiceRequest {

    private static final int DEFAULT_CONNECTION_TIMEOUT = 10*1000;
    private static final int DEFAULT_READ_TIMEOUT = 20*1000;

    public static RestTemplate getDefaultRestTemplate(boolean useGson, Integer... timeout){
        RestTemplate restTemplate = new RestTemplate(true);
        if(useGson) {
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
        }
        HttpComponentsClientHttpRequestFactory restTemplateFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(restTemplateFactory);
        if(timeout != null && timeout.length > 0){
            restTemplateFactory.setConnectTimeout(timeout[0]);
            restTemplateFactory.setReadTimeout(DEFAULT_READ_TIMEOUT);
        }
        else{
            restTemplateFactory.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT);
            restTemplateFactory.setReadTimeout(DEFAULT_READ_TIMEOUT);
        }
        return restTemplate;
    }

    /**
     *
     * @param context
     * @param genericException
     * @param logUnknown Print unknown exception stacktrace
     * @return Plain and understandable by humans error.
     */
    public static String serviceRequestExceptionManager(Context context, Exception genericException, boolean logUnknown, String... unknownErrorMessage){
        String result;
        try{
            throw genericException;
        }
        catch(ResourceAccessException ex){
            if(ex.getMessage().contains("refused")){
                result = context.getResources().getString(R.string.default_404_server_error);
            }
            else if(ex.getCause() instanceof ConnectTimeoutException){
                result = context.getResources().getString(R.string.timeout_error);
            }
            else{
                result = context.getResources().getString(R.string.default_client_connection_error);
            }
        }
        catch (HttpClientErrorException e) {
            if(e.getMessage().contains("401 Unauthorized")){
                return context.getString(R.string.default_unauthorized_error);
            }
            if(e.getMessage().contains("404")){
                result = context.getResources().getString(R.string.default_404_server_error);
            }
            else {
                result = context.getString(R.string.default_generic_error);
            }
        }
        catch (HttpServerErrorException e) {
            result = context.getResources().getString(R.string.default_404_server_error);
        }
        catch (HttpMessageNotReadableException ex){
            result = context.getResources().getString(R.string.default_404_server_error);
        }
        catch (Exception e) {
            if(unknownErrorMessage != null && unknownErrorMessage.length >0) {
                result = unknownErrorMessage[0];
            }
            else{
                result = context.getString(R.string.default_generic_error);
            }
        }
        if(logUnknown) {
            Log.w("WebServiceRequest", "Erro desconhecido", genericException);
        }
        else{
            Log.d("WebServiceRequest", "Erro desconhecido", genericException);
        }
        return result;
    }
}
