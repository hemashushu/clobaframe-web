package org.archboy.clobaframe.web.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.regex.Pattern;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author yang
 */
public class RouteDefinition {

	private Collection<Pattern> urlPatterns;
	private Collection<RequestMethod> requestMethods;
	private Object controller;
	private Method method; // the object function
	private Collection<ParameterInfo> parameterInfos;
	private Class<?> returnType;
	private boolean responseBody;
	
	public static class ParameterInfo {
		private Class<?> clazz;
		private Annotation annotation;

		public ParameterInfo(Class<?> clazz, Annotation annotation) {
			this.clazz = clazz;
			this.annotation = annotation;
		}

		public Class<?> getClazz() {
			return clazz;
		}

		public Annotation getAnnotation() {
			return annotation;
		}
	}

	public RouteDefinition(Collection<Pattern> urlPatterns, Collection<RequestMethod> requestMethods, Object controller, Method method, Collection<ParameterInfo> parameterInfos, Class<?> returnType, boolean responseBody) {
		this.urlPatterns = urlPatterns;
		this.requestMethods = requestMethods;
		this.controller = controller;
		this.method = method;
		this.parameterInfos = parameterInfos;
		this.returnType = returnType;
		this.responseBody = responseBody;
	}
	
	public Object getController() {
		return controller;
	}

	public Method getMethod() {
		return method;
	}

	public Collection<ParameterInfo> getParameterInfos() {
		return parameterInfos;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public boolean isResponseBody() {
		return responseBody;
	}

	public Collection<RequestMethod> getRequestMethods() {
		return requestMethods;
	}

	public Collection<Pattern> getUrlPatterns() {
		return urlPatterns;
	}
}
