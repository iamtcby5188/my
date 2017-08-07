package com.sumscope.bab.quote.commons.util;

/**
 * Created by fan.bai on 2017/3/7.
 * 获取当前系统命名，hostname+port
 * 在Linux系统中，hostname既计算机别名
 * Docker系统中为内部ID
 * Windows系统则无法取到
 */
public final class FlushCacheSourceUtil {

    private FlushCacheSourceUtil(){}

    public static String getSourceName(){
        String port = System.getProperty("port");
        String hostname = System.getProperty("hostname");
        if(hostname !=null){
            return hostname + ":" + port;
        }
        return null;
    }
}
