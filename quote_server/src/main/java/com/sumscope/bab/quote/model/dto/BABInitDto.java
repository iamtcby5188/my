package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.enums.BABQuoteType;

/**
 * Created by Administrator on 2016/12/19.
 * 应用程序初始化时，需要由Web传递给服务端的必要信息。
 */
public class BABInitDto {

    /**
     * 报价类型
     */
    private BABQuoteType babQuoteType;

    /**
     * 用户登录必要数据
     */
    private LoginUserDto user;

    public BABQuoteType getBabQuoteType() {
        return babQuoteType;
    }

    public void setBabQuoteType(BABQuoteType babQuoteType) {
        this.babQuoteType = babQuoteType;
    }

    public LoginUserDto getUser() {
        return user;
    }

    public void setUser(LoginUserDto user) {
        this.user = user;
    }
}
