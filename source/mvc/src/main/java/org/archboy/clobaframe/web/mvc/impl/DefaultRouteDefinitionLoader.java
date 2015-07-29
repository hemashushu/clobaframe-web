package org.archboy.clobaframe.web.mvc.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.web.mvc.RouteDefinition;
import org.archboy.clobaframe.web.mvc.RouteDefinitionLoader;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author yang
 */
public class DefaultRouteDefinitionLoader implements RouteDefinitionLoader {

	@Inject
	private ListableBeanFactory listableBeanFactory;

	public DefaultRouteDefinitionLoader() {
	}
	
	public DefaultRouteDefinitionLoader(ListableBeanFactory beanFactory) {
		this.listableBeanFactory = beanFactory;
	}
	
	@Override
	public List<RouteDefinition> list() {	
		List<RouteDefinition> routeDefinitions = new ArrayList<>();
		
		// list all controller objects.
		Map<String, Object> nameControllers = listableBeanFactory.getBeansWithAnnotation(Controller.class);
		if (nameControllers.isEmpty()) {
			return routeDefinitions;
		}
		
		// sort controller
		List<Object> controllers = new ArrayList<>(nameControllers.values());
		OrderComparator.sort(controllers);
		
		for (Object controller : controllers) {
			Class<?> clazz = controller.getClass();
			Method[] methods = clazz.getDeclaredMethods();
			
			for (Method method : methods) {
				if (method.isAnnotationPresent(RequestMapping.class)) {
					RouteDefinition routeDefinition = loadRouteDefinition(controller, method);
					routeDefinitions.add(routeDefinition);
				}
			}
		}
		
		return routeDefinitions;
	}

	private RouteDefinition loadRouteDefinition(Object controller, Method method) {
		
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		
		// get mapping name
		String routeMappingName = requestMapping.name();
		if (StringUtils.isEmpty(routeMappingName)) {
			routeMappingName = controller.getClass().getSimpleName() + "." + method.getName();
		}
		
		// get url pattern
		String[] urls = requestMapping.value();
		Collection<Pattern> urlPatterns = new ArrayList<>();
		
		for(String url : urls){
			urlPatterns.add(Pattern.compile(url));
		}
		
		// get request methods
		RequestMethod[] requestMethodArray = requestMapping.method();
		Collection<RequestMethod> requestMethods = new ArrayList<>();
		requestMethods.addAll(Arrays.asList(requestMethodArray));
		
		if (requestMethods.isEmpty()) {
			requestMethods.add(RequestMethod.GET); // default request method.
		}
		
		// get function params
		Parameter[] parameters = method.getParameters();
		Collection<RouteDefinition.ParameterInfo> parameterInfos = new ArrayList<>();
		for(Parameter parameter: parameters) {
			Class<?> paramType = parameter.getType();
			Annotation[] paramAnnotations = parameter.getDeclaredAnnotations();
			String name = parameter.getName();
			RouteDefinition.ParameterInfo parameterInfo = new RouteDefinition.ParameterInfo(
					name,
					paramType,
					(paramAnnotations.length == 0 ? null : paramAnnotations[0]));
			parameterInfos.add(parameterInfo);
		}
		
		// get return type
		Class<?> returnType = method.getReturnType();
		boolean responseBody = method.isAnnotationPresent(ResponseBody.class);
		RouteDefinition routeDefinition = new RouteDefinition(routeMappingName,
				urlPatterns, requestMethods,
				controller, method, parameterInfos,
				returnType, responseBody);
		return routeDefinition;
	}

}
