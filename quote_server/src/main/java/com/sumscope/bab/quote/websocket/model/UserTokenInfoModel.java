package com.sumscope.bab.quote.websocket.model;

/**
 * Created by Administrator on 2017/1/11.
 */
public class UserTokenInfoModel {

    private String userid;
    private String username;
    private String token;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
