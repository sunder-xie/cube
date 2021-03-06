package com.sharingif.cube.communication.transport.transform;

import com.sharingif.cube.core.transport.exception.MarshallerException;
import com.sharingif.cube.core.transport.transform.Marshaller;
import com.sharingif.cube.core.transport.transform.Transform;

/**
 * ProxyInterfaceHandlerMethodCommunicationTransform
 * 2017年5月17日 下午5:17:14
 * @author Joly
 * @version v1.0
 * @since v1.0
 */
public class ProxyInterfaceHandlerMethodCommunicationTransform<MO,CO,UO> implements Transform<Object[], MO, MethodParameterArgument<Object[], CO>, UO> {
	
	private Marshaller<Object[], MO> marshaller;
	private Marshaller<MethodParameterArgument<Object[], CO>, UO> unmarshaller;
	
	public Marshaller<Object[], MO> getMarshaller() {
		return marshaller;
	}
	public void setMarshaller(Marshaller<Object[], MO> marshaller) {
		this.marshaller = marshaller;
	}
	public Marshaller<MethodParameterArgument<Object[], CO>, UO> getUnmarshaller() {
		return unmarshaller;
	}
	public void setUnmarshaller(Marshaller<MethodParameterArgument<Object[], CO>, UO> unmarshaller) {
		this.unmarshaller = unmarshaller;
	}
	@Override
	public MO marshaller(Object[] data) throws MarshallerException {
		return marshaller.marshaller(data);
	}
	@Override
	public UO unmarshaller(MethodParameterArgument<Object[], CO> data) throws MarshallerException {
		return unmarshaller.marshaller(data);
	}
	

}
