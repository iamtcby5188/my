package com.sumscope.bab.quote.model.model;

import com.sumscope.bab.quote.commons.FlushCacheEnum;

/**
 * Created by fan.bai on 2017/3/6.
 * 使用总线消息传递缓存刷新信息。本类用于记录哪些缓存应被刷新，并且记录对应的数据。
 */
public class FlushCacheWrapper {
    /**
     * 缓存消息发送方名称，一般取：hostname + port
     */
    private String source;
    /**
     * 缓存更新的目标
     */
    private FlushCacheEnum target;

    /**
     * 对应数据的java类型
     */
    private String classFullName;

    /**
     * 对应数据的Json字符串
     */
    private String dataJsonString;

    public FlushCacheEnum getTarget() {
        return target;
    }

    public void setTarget(FlushCacheEnum target) {
        this.target = target;
    }

    public String getDataJsonString() {
        return dataJsonString;
    }

    public void setDataJsonString(String dataJsonString) {
        this.dataJsonString = dataJsonString;
    }

    public String getClassFullName() {
        return classFullName;
    }

    public void setClassFullName(String classFullName) {
        this.classFullName = classFullName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
