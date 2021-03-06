package com.sharingif.cube.communication.http.transport.transform;

import java.util.Map;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharingif.cube.communication.JsonModel;
import com.sharingif.cube.core.config.CubeConfigure;
import com.sharingif.cube.core.handler.bind.support.BindingInitializer;
import com.sharingif.cube.core.transport.exception.MarshallerException;
import com.sharingif.cube.core.transport.transform.Marshaller;

/**
 * JsonUnmarshaller
 * 2017年1月7日 下午8:41:59
 * @author Joly
 * @version v1.0
 * @since v1.0
 */
public class StringToJsonModelMarshaller implements Marshaller<String, JsonModel<Map<String, Object>>> {
	
	private ObjectMapper objectMapper;
	private BindingInitializer bindingInitializer;
	
	public StringToJsonModelMarshaller() {
		objectMapper = new ObjectMapper();
		objectMapper.setTimeZone(TimeZone.getTimeZone(CubeConfigure.DEFAULT_TIME_ZONE));
	}
	
	public StringToJsonModelMarshaller(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	public BindingInitializer getBindingInitializer() {
		return bindingInitializer;
	}

	public void setBindingInitializer(BindingInitializer bindingInitializer) {
		this.bindingInitializer = bindingInitializer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonModel<Map<String, Object>> marshaller(String data) throws MarshallerException {
		try {
			return new JsonModel<Map<String, Object>>(objectMapper.readValue(data, Map.class));
		} catch (Exception e) {
			throw new MarshallerException("marshaller json to object error", e);
		}
	}
	
}
