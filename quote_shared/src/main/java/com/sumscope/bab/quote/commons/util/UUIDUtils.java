package com.sumscope.bab.quote.commons.util;

import java.util.UUID;

/**
 * Created by shaoxu.wang on 2016/12/16.
 * UUID tools
 */
public final class UUIDUtils {
    private UUIDUtils() {
    }

    /**
     * @return 用于ID的UUID字符串，取消了其中的“-”
     */
    public static String generatePrimaryKey() {
        String id = UUID.randomUUID().toString();
        return id.replace("-", "");
    }
}
