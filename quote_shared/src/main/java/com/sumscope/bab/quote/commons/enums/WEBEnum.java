package com.sumscope.bab.quote.commons.enums;

/**
 * 当某一个枚举用于前端页面显示时，由服务端将枚举信息统一发送前端。这类枚举需要实现该接口，返回code和显示值。
 */
public interface WEBEnum {

	String getCode();

	String getDisplayName();

}
