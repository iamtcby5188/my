package com.sumscope.bab.quote.model.dto;

/**
 * Created by fan.bai on 2017/2/3.
 * 用于服务端向Web端传输SSR报价单所属票据图片数据Dto
 */
public class SSRQuoteImgDto {
    /**
     * 报价单ID
     */
    private String id;
    /**
     * Base64编码的图片数据
     */
    private String base64Img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBase64Img() {
        return base64Img;
    }

    public void setBase64Img(String base64Img) {
        this.base64Img = base64Img;
    }
}
