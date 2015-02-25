package com.cea.utils.web;

/**
 * Created by carlos.araujo on 10/02/2015.
 */
public interface DownloadProgressListener {
    void updateProgress(double downloadedSize, double totalSize);
}
