package com.sharingif.cube.web.vert.x.view;


import com.sharingif.cube.communication.MediaType;
import com.sharingif.cube.communication.view.AbstractJsonView;
import com.sharingif.cube.core.exception.handler.ExceptionContent;
import com.sharingif.cube.core.request.RequestInfo;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;

/**
 * VertXJsonView
 * 2016年12月28日 下午3:52:05
 * @author Joly
 * @version v1.0
 * @since v1.0
 */
public class VertXJsonView extends AbstractJsonView<RoutingContext> {
	
	@Override
	public void view(RequestInfo<RoutingContext> requestInfo, Object returnValue,ExceptionContent exceptionContent) {
		
		Buffer buffer = Buffer.buffer(getResponseData(returnValue, exceptionContent == null ? null: exceptionContent.getCubeException()));
		
		requestInfo.getRequest().response()
		.putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
		.putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(buffer.length()))
		.write(buffer)
		.end();
		
	}

}
