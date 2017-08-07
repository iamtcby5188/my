package com.sumscope.bab.quote.service;


/**
 * Created by fan.bai on 2017/3/28.
 * 用于记录是否是主节点信息。一个应用程序只要一个静态变量记录，该变量由启动时命令参数决定取值
 */
public class ApplicationEnviromentConstant {
    private static boolean primary;

    public static boolean isPrimary() {
        return primary;
    }

    public static void setPrimary(boolean primaryb) {
        primary = primaryb;
    }
}
