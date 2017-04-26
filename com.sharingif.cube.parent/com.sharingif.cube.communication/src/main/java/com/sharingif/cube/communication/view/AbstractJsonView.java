package com.sharingif.cube.communication.view;

import java.util.TimeZone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharingif.cube.communication.JsonModel;
import com.sharingif.cube.core.exception.CubeRuntimeException;
import com.sharingif.cube.core.exception.ICubeException;
import com.sharingif.cube.core.exception.handler.ExceptionContent;

/**
 * AbstractJsonView
 * 2016年12月28日 下午5:02:41
 * @author Joly
 * @version v1.0
 * @since v1.0
 */
public abstract class AbstractJsonView<T,O extends ExceptionContent> implements View<T,O> {
	
	private String exceptionMessageName = "_exceptionMessage";
	private String exceptionLocalizedMessageName = "_exceptionLocalizedMessage";
	private String tranStatusName ="_tranStatus";
	
	private ObjectMapper objectMapper;
	
	public AbstractJsonView() {
		objectMapper = new ObjectMapper();
		objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+0800"));			// 中国上海
	}
	
	public AbstractJsonView(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	public String getExceptionMessageName() {
		return exceptionMessageName;
	}
	public void setExceptionMessageName(String exceptionMessageName) {
		this.exceptionMessageName = exceptionMessageName;
	}
	public String getExceptionLocalizedMessageName() {
		return exceptionLocalizedMessageName;
	}
	public void setExceptionLocalizedMessageName(
			String exceptionLocalizedMessageName) {
		this.exceptionLocalizedMessageName = exceptionLocalizedMessageName;
	}
	public String getTranStatusName() {
		return tranStatusName;
	}
	public void setTranStatusName(String tranStatusName) {
		this.tranStatusName = tranStatusName;
	}

	protected String objectoJson(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new CubeRuntimeException("convert object to json error", e);
		}
	}
	
	protected String getResponseData(Object value, ICubeException exception) {
		
		JsonModel<Object> jsonModel = null;
		
		if(exception == null) {
			jsonModel = new JsonModel<Object>(true, null, null, value);
		} else {
			jsonModel = new JsonModel<Object>(false, exception.getMessage(), exception.getLocalizedMessage(), null);
		}
		
		return objectoJson(jsonModel);
	}

}