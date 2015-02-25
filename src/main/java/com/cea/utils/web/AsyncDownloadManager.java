package com.cea.utils.web;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * Created by carlos.araujo on 10/02/2015.
 */
public class AsyncDownloadManager {
    public static void download(HttpUriRequest request, ByteArrayBuffer buffer, DownloadProgressListener listener, UsernamePasswordCredentials... credentials) throws IOException {

        DefaultHttpClient client = new DefaultHttpClient();
        if(credentials.length > 0){
            client.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials[0]);
        }
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        Header header = response.getHeaders("Content-Length")[0];
        double totalSize = Integer.parseInt(header.getValue());
        double downloadedSize = 0;

        if(entity != null){
            byte[] byteBuffer = new byte[1024*1024];
            InputStream stream = entity.getContent();
            int readedBytes;
            do {
                readedBytes = stream.read(byteBuffer);
                if(readedBytes > 0) {
                    buffer.append(byteBuffer, 0, readedBytes);
                    downloadedSize += readedBytes;
                    if(listener != null) {
                        listener.updateProgress(downloadedSize, totalSize);
                    }
                }
            }while (readedBytes > 0);
        }
    }

    public static String convertToHumanFormat(double bytesSize, Format format){
        DecimalFormat formatter = new DecimalFormat("0.00");
        return formatter.format(bytesSize / format.unit());
    }
}
