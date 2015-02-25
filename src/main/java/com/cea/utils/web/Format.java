package com.cea.utils.web;

/**
 * Created by carlos.araujo on 10/02/2015.
 */
public enum Format {
    KB, MB, GB;

    public double unit() {
        if(this.equals(KB)){
            return 1024;
        }
        if(this.equals(MB)){
            return 1024*1024;
        }
        if(this.equals(GB)){
            return 1024*1024*1024;
        }
        return -1;
    }
}
