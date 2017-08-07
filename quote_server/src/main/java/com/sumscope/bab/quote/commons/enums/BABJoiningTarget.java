package com.sumscope.bab.quote.commons.enums;

/**
 * 用户关联方向枚举
 */
public enum BABJoiningTarget implements WEBEnum {

    /**
     * 让输入的用户获取替我报价的权限。
     */
    PARENT("母账号设置"),

    /**
     * 获取替输入用户报价的权限
     */
    CHILD("子账号设置");

    private String name;


    BABJoiningTarget( String name) {
        this.name = name;
    }


    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getDisplayName() {
        return name;
    }
}
