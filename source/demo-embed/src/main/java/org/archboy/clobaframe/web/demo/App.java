package org.archboy.clobaframe.web.demo;

import java.util.EnumSet;
import java.util.Locale;
import javax.servlet.DispatcherType;
import org.archboy.clobaframe.ioc.BeanFactoryClosedEvent;
import org.archboy.clobaframe.ioc.impl.DefaultBeanFactory;
import org.archboy.clobaframe.setting.application.ApplicationSetting;
import org.archboy.clobaframe.setting.application.impl.DefaultApplicationSetting;
import org.archboy.clobaframe.web.i18n.CookieLocaleInterceptor;
import org.archboy.clobaframe.web.mvc.DispatcherServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.context.request.RequestContextListener;

/**
 * 
 * @author yang
 */
public class App 
{
	//private ApplicationContext applicationContext;
	//private Server server;
	//private ApplicationSetting applicationSetting;
	
    public static void main( String[] args ) throws Exception
    {
       App app = new App();
	   app.start();
    }
	
	public void start() throws Exception{
		
		long start = System.currentTimeMillis();
		
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		ApplicationSetting applicationSetting = new DefaultApplicationSetting(
				resourceLoader, "", null, "classpath:root.properties", null, 
				"classpath:clobaframe.properties", 
				"classpath:web.properties");
		
		BeanFactory beanFactory = new DefaultBeanFactory(resourceLoader, applicationSetting);
		((DefaultBeanFactory)beanFactory).addApplicationListener(new ApplicationListener<BeanFactoryClosedEvent>() {

			@Override
			public void onApplicationEvent(BeanFactoryClosedEvent event) {
				// app close
				System.out.println("Application exit.");
			}
		});

		// build dispatcher servlet
		DispatcherServlet dispatcherServlet = beanFactory.getBean(DispatcherServlet.class);
		RequestContextListener requestContextListener = beanFactory.getBean(RequestContextListener.class);
		//CookieLocaleInterceptor cookieLocaleFilter = beanFactory.getBean(CookieLocaleInterceptor.class);
				
		ServletHolder servletHolder = new ServletHolder("dispatcher", dispatcherServlet);
		
		// build servlet context handler
		ServletContextHandler handler = new ServletContextHandler(); //ServletContextHandler.SESSIONS);
		handler.setContextPath("/");
		handler.addEventListener(requestContextListener);
		handler.addServlet(servletHolder, "/");
		//handler.addFilter(new FilterHolder(cookieLocaleFilter), "/", EnumSet.of(DispatcherType.REQUEST));
		//handler.setResourceBase(new ClassPathResource("src/main/webapp").getPath());
		
		// start http server
		Server server = new Server(8080);
		server.setHandler(handler);
		
		try{
			server.start();
			
			long span = System.currentTimeMillis() - start;
			System.out.println("Server start in " + span + " ms.");
			System.out.println("Server listening at TCP port 8080.");
			
			//server.join();
			System.in.read(); // wait for exit
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			((DefaultBeanFactory)beanFactory).close();
			server.stop();
		}
		
		//((DefaultBeanFactory)beanFactory).close();
	}
}
