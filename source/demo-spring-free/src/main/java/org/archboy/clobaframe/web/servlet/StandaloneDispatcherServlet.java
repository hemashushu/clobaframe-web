package org.archboy.clobaframe.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.ioc.impl.DefaultBeanFactory;
import org.archboy.clobaframe.setting.application.ApplicationSetting;
import org.archboy.clobaframe.setting.application.impl.DefaultApplicationSetting;
import org.archboy.clobaframe.web.mvc.DispatcherServlet;
import org.archboy.clobaframe.web.mvc.RouteDefinitionLoader;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.context.support.ServletContextResourceLoader;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;

/**
 *
 * @author yang
 */
public class StandaloneDispatcherServlet extends DispatcherServlet{

	private ListableBeanFactory beanFactory;
	
	@Override
	public void init() throws ServletException {
		
		// get config
		ServletConfig servletConfig = getServletConfig();
		//ServletContext servletContext = getServletContext();

		String rootConfigLocation = servletConfig.getInitParameter("rootConfigLocation");
		String otherConfigLocationString = servletConfig.getInitParameter("otherConfigLocations");
		
		if (rootConfigLocation != null) {
			rootConfigLocation = rootConfigLocation.trim();
		}
		
		if (otherConfigLocationString != null) {
			otherConfigLocationString.trim();
		}
		
		String[] otherConfigLocations = null;
		if (StringUtils.isNotEmpty(otherConfigLocationString)){
			String[] locations = otherConfigLocationString.split("\n");
			otherConfigLocations = new String[locations.length];
			for(int idx = 0; idx < locations.length; idx++) {
				otherConfigLocations[idx] = locations[idx].trim();
			}
		}

		try{
			// build settings
			ResourceLoader resourceLoader = new ServletContextResourceLoader(getServletContext());
			ApplicationSetting applicationSetting = new DefaultApplicationSetting(
					resourceLoader, "", null, rootConfigLocation, null, otherConfigLocations);

			// build bean factory
			this.beanFactory = new DefaultBeanFactory(resourceLoader, applicationSetting);

			RouteDefinitionLoader routeDefinitionLoader = beanFactory.getBean(RouteDefinitionLoader.class);
			super.setRouteDefinitionLoader(routeDefinitionLoader);

			ViewResolver viewResolver = beanFactory.getBean(ViewResolver.class);
			super.setViewResolver(viewResolver);
			
			Map<String, HandlerExceptionResolver> namedHandlerExceptionResolvers = beanFactory.getBeansOfType(HandlerExceptionResolver.class);
			if (!namedHandlerExceptionResolvers.isEmpty()) {
				List<HandlerExceptionResolver> handlerExceptionResolvers = new ArrayList<>(namedHandlerExceptionResolvers.values());
				super.setHandlerExceptionResolvers(handlerExceptionResolvers);
			}
			
			Map<String, HandlerInterceptor> namedHandlerInterceptors = beanFactory.getBeansOfType(HandlerInterceptor.class);
			if (!namedHandlerInterceptors.isEmpty()) {
				List<HandlerInterceptor> handlerInterceptors = new ArrayList<>(namedHandlerInterceptors.values());
				super.setHandlerInterceptors(handlerInterceptors);
			}

			super.init();
			
		}catch(Exception e){
			throw new ServletException(e);
		}
	}

	@Override
	public void destroy() {
		try{
			((DefaultBeanFactory)beanFactory).close();
		}catch(IOException e) {
			log("Close servlet error.", e);
		}
	}
}
