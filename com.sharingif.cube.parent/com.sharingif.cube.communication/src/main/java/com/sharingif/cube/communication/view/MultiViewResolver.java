package com.sharingif.cube.communication.view;

import com.sharingif.cube.communication.view.exception.NoViewFoundException;
import com.sharingif.cube.core.exception.handler.ExceptionContent;
import com.sharingif.cube.core.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ViewResolver 集合
 * 2017/5/20 下午8:54
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 */
public class MultiViewResolver implements ViewResolver<Object> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("rawtypes")
	private List<ViewResolver> viewResolvers;

    @SuppressWarnings("rawtypes")
	public List<ViewResolver> getViewResolvers() {
        return viewResolvers;
    }

    @SuppressWarnings("rawtypes")
	public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    @SuppressWarnings("unchecked")
	@Override
    public View<Object> resolveView(RequestInfo<Object> requestInfo, Object returnValue, ExceptionContent exceptionContent) {
        for(ViewResolver<Object> viewResolver : getViewResolvers()) {
            View<Object> view = viewResolver.resolveView(requestInfo, returnValue, exceptionContent);
            if(view != null){
                return view;
            }
        }

        logger.error("No view found for request");
        throw new NoViewFoundException();
    }
}
