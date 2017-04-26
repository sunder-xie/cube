package com.sharingif.cube.communication.http.transport.transform;

import java.util.TimeZone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharingif.cube.communication.transport.transform.Marshaller;
import com.sharingif.cube.communication.transport.transform.exception.MarshallerException;

/**
 * 对象数组转json字符串
 * 2017年1月7日 下午8:32:16
 * @author Joly
 * @version v1.0
 * @since v1.0
 */
public class ObjectToJsonStringMarshaller implements Marshaller<Object[], String> {
	
	private ObjectMapper objectMapper;
	
	public ObjectToJsonStringMarshaller() {
		objectMapper = new ObjectMapper();
		objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+0800"));			// 中国上海
	}
	
	public ObjectToJsonStringMarshaller(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public String marshaller(Object[] data) throws MarshallerException {
		try {
			if(data == null || data.length == 0) {
				return null;
			}
			
			return objectMapper.writeValueAsString((data.length > 1) ? data : data[0]);
		} catch (Exception e) {
			throw new MarshallerException("marshaller object to json error", e);
		}
	}

}