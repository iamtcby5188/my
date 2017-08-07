package com.sumscope.bab.quote.model.dto;

import java.util.List;

/**
 * Created by Administrator on 2017/2/28.
 * 返回给web端JoinUserDto
 */
public class JoinUserResponseDto {
    /**
     * 返回给web端JoinUser的关系信息
     */
    private List<AvailableContactDto> availableContactDto;
    /**
     * 返回给web端当前用户信息
     */
    private AppInitialDataDto userInfo;

    public List<AvailableContactDto> getAvailableContactDto() {
        return availableContactDto;
    }

    public void setAvailableContactDto(List<AvailableContactDto> availableContactDto) {
        this.availableContactDto = availableContactDto;
    }

    public AppInitialDataDto getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(AppInitialDataDto userInfo) {
        this.userInfo = userInfo;
    }
}
