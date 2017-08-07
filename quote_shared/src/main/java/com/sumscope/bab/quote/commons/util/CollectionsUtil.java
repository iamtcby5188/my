package com.sumscope.bab.quote.commons.util;

import java.util.Collection;

/**
 * Created by shaoxu.wang on 2016/12/16.
 * 工具类
 */
public final class CollectionsUtil {

    private CollectionsUtil(){

    }
    /**
     * 判断一个List是否为空
     */
    public static boolean isEmptyOrNullCollection(Collection list) {
        return list == null || list.size() == 0;
    }

}
