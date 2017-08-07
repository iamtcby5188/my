package com.sumscope.bab.quote.model.dto;

import java.util.Date;

/**
 * Created by Administrator on 2017/3/2.
 * 返回给web端tokenModelDto防止重复提交使用
 */
public class TokenModelDto {
    private String token;//唯一标识
    private Date effectiveDate;//生效时间
    private Date expiredDate;//失效时间
    private String fromProject;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getFromProject() {
        return fromProject;
    }

    public void setFromProject(String fromProject) {
        this.fromProject = fromProject;
    }
}
