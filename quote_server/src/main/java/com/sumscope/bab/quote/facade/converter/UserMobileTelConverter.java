package com.sumscope.bab.quote.facade.converter;

import com.sumscope.iam.edmclient.edmsource.dto.UserContactInfoRetDTO;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by shaoxu.wang on 2017/1/11.
 * 用户电话字符串工具
 */
public final class UserMobileTelConverter {
    private UserMobileTelConverter() {
    }

    public static String getUserMobileTel(UserContactInfoRetDTO usersContactDto) {
        if (usersContactDto != null) {
            String mobile = usersContactDto.getMobile() == null ? "" : usersContactDto.getMobile();
            String telephone = usersContactDto.getTelephone() == null ? "" : usersContactDto.getTelephone();
            if (mobile.contains(";")) {
                mobile = mobile.replace(";", "");
            }
            if (telephone.contains(";")) {
                telephone = telephone.replace(";", "");
            }
            if (StringUtils.isNotBlank(mobile) && StringUtils.isNotBlank(telephone)) {
                return mobile + "," + telephone;
            } else {
                return mobile + telephone;
            }
        }
        return "";
    }
}
