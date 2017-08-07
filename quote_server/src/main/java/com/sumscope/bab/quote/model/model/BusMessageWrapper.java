package com.sumscope.bab.quote.model.model;

import com.sumscope.bab.quote.commons.enums.EditMode;
import com.sumscope.optimus.commons.util.JsonUtil;

public class BusMessageWrapper {

	private EditMode messageType;

	private String source;

	private Object data;

	public EditMode getMessageType() {
		return messageType;
	}

	public void setMessageType(EditMode messageType) {
		this.messageType = messageType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String formatJsonString() {
		String valString = JsonUtil.writeValueAsString(getData());

		return "{\"source\" : " + getSource() + ", \"editMode\" : " + getMessageType().toString() + ", \"object\" : " + valString;
	}
}
