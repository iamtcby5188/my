package com.sumscope.bab.quote.commons.model;

import com.alibaba.fastjson.JSONObject;
import com.sumscope.bab.quote.commons.enums.EditMode;
import com.sumscope.optimus.commons.util.JsonUtil;

/**
 * Created by shaoxu.wang on 2016/12/27.
 */
public class BABQuoteWrapper {
    /**
     * 数据源 BABQuote
     */
    private String source;

    /**
     * 操作类型 (新增/更新/删除)
     */
    private EditMode editMode;


    /**
     * 数据类型
     */
    private String quoteType;

    /**
     * 数据
     */
    private Object object;



    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public EditMode getEditMode() {
        return editMode;
    }

    public void setEditMode(EditMode editMode) {
        this.editMode = editMode;
    }

    public String getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String formatJsonToString() {
        JSONObject object=new JSONObject();
        object.put("source",getSource());
        object.put("editMode", getEditMode());
        object.put("quoteType",getQuoteType());
        object.put("object",JsonUtil.writeValueAsString(getObject()));
        return object.toJSONString();
    }
}
