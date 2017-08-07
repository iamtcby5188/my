package com.sumscope.bab.quote.model.dto;

/**
 * 用于用户登录的Dto，当前端发起登录请求时，仅需要userName 和password字段。服务端返回时返回其余字段。
 */
public class LoginUserDto extends IAMUserReferenceDto {

    /**
     * token过期日期时间
     */
    private int tokenExpiredTime;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 加密后的密码
     */
    private String password;

    /**
     * 所在部门
     */
    private String dept;

    public int getTokenExpiredTime() {
        return tokenExpiredTime;
    }

    public void setTokenExpiredTime(int tokenExpiredTime) {
        this.tokenExpiredTime = tokenExpiredTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
}
