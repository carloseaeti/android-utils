package com.cea.utils.web.request;

import android.content.Context;
import android.util.Log;

import com.cea.utils.R;

import org.apache.http.HttpStatus;

/**
 * Created by Carlos on 20/08/2015.
 */
public class NetworkExceptionHandler {

    public static String handle(Context context, Exception exception, boolean logUnknown, String... unknownErrorMessage){
        try{
            throw exception;
        }
        catch (UnknownHttpException ex){
            return context.getResources().getString(R.string.default_client_connection_error);
        }
        catch(ServerException ex){
            if(ex.getStatusCode() == HttpStatus.SC_UNAUTHORIZED){
                return context.getString(R.string.default_unauthorized_error);
            }
            else{
                return context.getResources().getString(R.string.default_404_server_error);
            }
        }
        catch (Exception ex){
            return handleUnknownException(context, ex, logUnknown, unknownErrorMessage);
        }
    }

    private static String handleUnknownException(Context context, Exception ex, boolean logUnknown, String[] unknownErrorMessage) {
        logUnknown(ex, logUnknown);
        if(unknownErrorMessage != null && unknownErrorMessage.length > 0){
            return unknownErrorMessage[0];
        }
        return context.getString(R.string.default_generic_error);
    }

    private static void logUnknown(Exception ex, boolean logUnknown) {
        if(logUnknown) {
            Log.w("WebServiceRequest", "Erro desconhecido", ex);
        }
        else{
            Log.d("WebServiceRequest", "Erro desconhecido", ex);
        }
    }
}
