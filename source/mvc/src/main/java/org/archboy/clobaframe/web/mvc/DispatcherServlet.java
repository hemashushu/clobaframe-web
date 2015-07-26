package org.archboy.clobaframe.web.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 *
 * @author yang
 */
public class DispatcherServlet extends HttpServlet {
	
	@Inject
	private RouteManager routeManager;
	
	@Autowired(required = false)
	private List<HandlerExceptionResolver> handlerExceptionResolvers;
	
	@Inject
	private ViewResolver viewResolver;

	private List<RouteDefinition> routeDefinitions;
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

	public void setRouteManager(RouteManager routeManager) {
		this.routeManager = routeManager;
	}
	
	public void setViewResolver(ViewResolver viewResolver) {
		this.viewResolver = viewResolver;
	}

	public void setHandlerExceptionResolvers(List<HandlerExceptionResolver> handlerExceptionResolvers) {
		this.handlerExceptionResolvers = handlerExceptionResolvers;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		routeDefinitions = routeManager.list();
		
		if (handlerExceptionResolvers != null && !handlerExceptionResolvers.isEmpty()){
			OrderComparator.sort(handlerExceptionResolvers);
		}
	}

	@Override
	public void destroy() {
		//
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		try{
			handle(req, resp);
		}catch(Exception ex){
			
			if (!(ex instanceof InvocationTargetException)) {
				logger.error(ex.getMessage(), ex);
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
				return;
			}
			
			Exception te = (Exception)((InvocationTargetException)ex).getTargetException();
			
			// no exception resolver.
			if (handlerExceptionResolvers == null || handlerExceptionResolvers.isEmpty()) {
				//log(e.getMessage(), e);
				logger.error(te.getMessage(), te);
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, te.getMessage());
				return;
			}
			
			// try to get the match resolver.
			for (HandlerExceptionResolver resolver : handlerExceptionResolvers) {
				ModelAndView modelAndView = resolver.resolveException(req, resp, null, te);
				if (modelAndView != null){
					if (modelAndView.isEmpty()){
						// the handler process the response itself.
						return;
					}else{
						try {
							View view = viewResolver.resolveViewName(modelAndView.getViewName(), req.getLocale());
							if (view == null) {
								resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
										String.format("View [%s] not found.", modelAndView.getViewName()));
								return;
							}
							view.render(modelAndView.getModel(), req, resp);
						} catch (Exception tex) {
							logger.info(tex.getMessage(), tex);
							resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, tex.getMessage());
							return;
						}
					}
				}
			}
			
			// no match exception resolver.
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No match exception resolver.");
		}
	}
	
	private void handle(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		
		RouteMatcher routeMatcher = matcher(req);
		if (routeMatcher == null) {
			throw new FileNotFoundException(
					String.format("Resource [%s %s] not found.", req.getMethod(), req.getRequestURI()));
		}
		
		RouteDefinition definition = routeMatcher.getRouteDefinition();
		Matcher matcher = routeMatcher.getMatcher();
		Object controller = definition.getController();
		Method method = definition.getMethod();
		
		ExtendedModelMap model = new ExtendedModelMap();
		
		// prepare method args
		List<Object> params = new ArrayList<>();
		for(RouteDefinition.ParameterInfo parameterInfo : definition.getParameterInfos()) {
			
			String name = parameterInfo.getName();
			Class<?> clazz = parameterInfo.getClazz();
			Annotation annotation = parameterInfo.getAnnotation();
			
			if (annotation == null) {
				// parameter that without annotation
				if (clazz.equals(HttpServletRequest.class)){
					params.add(req);
				}else if (clazz.equals(HttpServletResponse.class)) {
					params.add(resp);
				}else if (clazz.equals(Locale.class)){
					params.add(req.getLocale());
				}else if (clazz.equals(InputStream.class)) {
					params.add(req.getInputStream()); // ServletInputStream
				}else if (clazz.equals(Reader.class)) {
					params.add(req.getReader()); // BufferedReader
				}else if (clazz.equals(OutputStream.class)) {
					params.add(resp.getOutputStream()); // ServletOutputStream
				}else if (clazz.equals(Writer.class)){
					params.add(resp.getWriter()); //PrintWriter
				}else if (clazz.equals(HttpMethod.class)) {
					params.add(HttpMethod.valueOf(req.getMethod()));
				}else if (clazz.equals(Map.class) || clazz.equals(Model.class)){
					params.add(model);
				}else {
					// no match type
					throw new IllegalArgumentException(
							String.format("No match type for the parameter [%s] in method [%s#%s].",
									name, 
									controller.getClass().getName(), 
									method.getName()));
				}
			}else{
				// parameter with annotation
				Class<?> annotationType = annotation.annotationType();
				if (annotationType.equals(RequestParam.class)){
					// handle @RequestParam
					Assert.isTrue(clazz.equals(String.class));
					
					RequestParam requestParam = (RequestParam)annotation;
					String value = req.getParameter(requestParam.value());
					if (value == null) {
						if (requestParam.required()){
							throw new IllegalArgumentException(
									String.format("Can not find the request parameter (in query string or post form field) [%s] in method [%s#%s].",
											requestParam.value(), 
											controller.getClass().getName(), 
											method.getName()));
						}else{
							params.add(
									requestParam.defaultValue().equals(ValueConstants.DEFAULT_NONE) ? 
											null : requestParam.defaultValue());
						}
					}else{
						params.add(value);
					}
				}else if (annotationType.equals(PathVariable.class)) {
					// handle @PathVariable
					Assert.isTrue(clazz.equals(String.class));
					
					PathVariable pathVariable = (PathVariable)annotation;
					String value = matcher.group(pathVariable.value());
					if (StringUtils.isEmpty(value)) {
						throw new IllegalArgumentException(
									String.format("Can not find the path variable [%s] in method [%s#%s].",
											pathVariable.value(), 
											controller.getClass().getName(), 
											method.getName()));	
					}else{
						params.add(value);
					}
				}else if (annotationType.equals(RequestBody.class)){
					// handle @RequestBody
					Assert.isTrue(clazz.equals(Map.class)); // only Map is supported currently.
					
					String encoding = (req.getCharacterEncoding() == null) ? "UTF-8" : req.getCharacterEncoding();
					String body = IOUtils.toString(req.getInputStream(), encoding);
					Map map = objectMapper.readValue(body, Map.class);
					params.add(map);
				}else{
					// unsupport annotation type
					throw new IllegalArgumentException(
							String.format("Unsupport annotation type [%s] in method [%s#%s].",
									annotationType.getName(),
									controller.getClass().getName(), 
									method.getName()));
				}
			}
		} // end building params
		
		// excute
		Object returnObject = method.invoke(controller, params.toArray());

		Class<?> clazz = definition.getReturnType();
		if (definition.isResponseBody()) {
			// return the object as response body

			if (clazz.equals(Void.TYPE)) {
				// do nothing
			}if (clazz.equals(String.class)) {
				resp.getWriter().write((String)returnObject);
			}else{
				resp.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
				objectMapper.writeValue(resp.getOutputStream(), returnObject);
			}
		}else {
			// return view
			if (clazz.equals(Void.TYPE)) {
				// do nothing
			}else if (clazz.equals(String.class)){
				View view = viewResolver.resolveViewName((String)returnObject, req.getLocale());
				if (view == null) {
					resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
							String.format("View [%s] not found.", returnObject));
				}else{
					view.render(model, req, resp);
				}
			}else if (clazz.equals(ModelAndView.class)){
				ModelAndView modelAndView = (ModelAndView)returnObject;
				View view = viewResolver.resolveViewName(modelAndView.getViewName(), req.getLocale());
				if (view == null) {
					resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
							String.format("View [%s] not found.", returnObject));
				}else{
					view.render(modelAndView.getModel(), req, resp);
				}
			}else{
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
							String.format("Does not support the return type [%s] in method [%s#%s].", 
									clazz.getName(),
									controller.getClass().getName(), 
									method.getName()));
			}
		}
	}
	
	private RouteMatcher matcher(HttpServletRequest request) {
		String path = request.getRequestURI();
		String method = request.getMethod();
		
		RequestMethod requestMethod = RequestMethod.valueOf(method);
		
		for(RouteDefinition definition : routeDefinitions) {
			if (definition.getRequestMethods().contains(requestMethod)) {
				for (Pattern pattern : definition.getUrlPatterns()){
					Matcher matcher = pattern.matcher(path);
					if (matcher.matches()){
						return new RouteMatcher(matcher, definition);
					}
				}
			}
		}
		
		return null;
	}

	private static class RouteMatcher {
		private Matcher matcher;
		private RouteDefinition routeDefinition;

		public RouteMatcher(Matcher matcher, RouteDefinition routeDefinition) {
			this.matcher = matcher;
			this.routeDefinition = routeDefinition;
		}

		public Matcher getMatcher() {
			return matcher;
		}

		public RouteDefinition getRouteDefinition() {
			return routeDefinition;
		}
	}
}
