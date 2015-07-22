package org.archboy.clobaframe.web.mvc.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.archboy.clobaframe.ioc.BeanFactory;
import org.archboy.clobaframe.web.mvc.RouteDefinition;
import org.archboy.clobaframe.web.mvc.RouteManager;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author yang
 */
public class RouteManagerImpl implements RouteManager {

	@Inject
	private BeanFactory beanFactory;
	
	private List<RouteDefinition> routeDefinitions = new ArrayList<>();
	
	@PostConstruct
	public void init() throws Exception {
		Collection<Object> controllers = beanFactory.listByAnnotation(Controller.class);
		if (controllers.isEmpty()) {
			return;
		}
		
		List<Object> allControllers = sortController(controllers);
		
		for (Object controller : allControllers) {
			
			Class<?> clazz = controller.getClass();
			Method[] methods = clazz.getDeclaredMethods();
			
			for (Method method : methods) {
				if (method.isAnnotationPresent(RequestMapping.class)) {
					RouteDefinition routeDefinition = buildDefinition(controller, method);
					routeDefinitions.add(routeDefinition);
				}
			}
		}
		
	}

	private RouteDefinition buildDefinition(Object c, Method method) {
		
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		
		// get url pattern
		String[] urls = requestMapping.value();
		Collection<Pattern> urlPatterns = new ArrayList<>();
		for(String url : urls){
			urlPatterns.add(Pattern.compile(url));
		}
		
		// get request methods
		Collection<RequestMethod> requestMethods = Arrays.asList(requestMapping.method());
		if (requestMethods.isEmpty()) {
			requestMethods.add(RequestMethod.GET); // default request method.
		}
		
		// get function params
		Parameter[] parameters = method.getParameters();
		Collection<RouteDefinition.ParameterInfo> parameterInfos = new ArrayList<>();
		for(Parameter parameter: parameters) {
			Class<?> paramType = parameter.getType();
			Annotation[] paramAnnotations = parameter.getDeclaredAnnotations();
			RouteDefinition.ParameterInfo parameterInfo = new RouteDefinition.ParameterInfo(
					paramType,
					(paramAnnotations.length == 0 ? null : paramAnnotations[0]));
			parameterInfos.add(parameterInfo);
		}
		
		// get return type
		Class<?> returnType = method.getReturnType();
		boolean responseBody = method.isAnnotationPresent(ResponseBody.class);
		RouteDefinition routeDefinition = new RouteDefinition(
				urlPatterns, requestMethods,
				c, method, parameterInfos,
				returnType, responseBody);
		return routeDefinition;
	}
	
	private List<Object> sortController(Collection<Object> controllers) {
		List<Object> unorderedControllers = new ArrayList<>();
		List<Ordered> orderedControllers = new ArrayList<>();
		
		for(Object c : controllers) {
			if (c instanceof Ordered) {
				orderedControllers.add((Ordered)c);
			}else{
				unorderedControllers.add(c);
			}
		}
		
		// sort 0-9
		orderedControllers.sort(new Comparator<Ordered>() {
			@Override
			public int compare(Ordered o1, Ordered o2){
				return o1.getOrder() - o2.getOrder();
			}
		});
		
		List<Object> allControllers = new ArrayList<>();
		allControllers.addAll(orderedControllers);
		allControllers.addAll(unorderedControllers);
		return allControllers;
	}
	
	@Override
	public RouteDefinition get(HttpServletRequest request) {
		String path = request.getRequestURI();
		String method = request.getMethod();
		
		RequestMethod requestMethod = RequestMethod.valueOf(method);
		
		for(RouteDefinition definition : routeDefinitions) {
			if (definition.getRequestMethods().contains(requestMethod)) {
				for (Pattern pattern : definition.getUrlPatterns()){
					if (pattern.matcher(path).matches()){
						return definition;
					}
				}
			}
		}
		
		return null;
	}
}
