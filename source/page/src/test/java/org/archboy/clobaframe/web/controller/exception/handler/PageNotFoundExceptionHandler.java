package org.archboy.clobaframe.web.controller.exception.handler;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.archboy.clobaframe.common.collection.DefaultObjectMap;
import org.archboy.clobaframe.common.collection.ObjectMap;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

/**
 * see also
 * {@link DefaultHandlerExceptionResolver},
 * {@link SimpleMappingExceptionResolver} and
 * {@link ExceptionHandlerExceptionResolver}.
 *
 * @author yang
 */
@Named
public class PageNotFoundExceptionHandler implements HandlerExceptionResolver {

	private static final String HEADER_EXPIRES = "Expires";
	private static final String HEADER_CACHE_CONTROL = "Cache-Control";

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
		HttpServletResponse response,
		Object handler, Exception ex) {

		if (ex instanceof FileNotFoundException) {
			// disable the client cache
			response.setHeader(HEADER_CACHE_CONTROL, "no-cache");
			response.setDateHeader(HEADER_EXPIRES, 1L);
			
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			
			ObjectMap viewModel = new DefaultObjectMap()
					.add("code", "notFound");
			
			return new ModelAndView("error", viewModel);
		}

		return null; // let other resolver to handle.
	}
}