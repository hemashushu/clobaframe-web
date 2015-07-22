package org.archboy.clobaframe.web.mvc;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ViewResolver;

/**
 *
 * @author yang
 */
public class DispatcherServlet extends HttpServlet {
	
	@Inject
	private RouteManager routeManager;
	
	public void setViewResolver(ViewResolver viewResolver) {
		
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		try{
			RouteDefinition routeDefinition = routeManager.get(req);
			if (routeDefinition == null) {
				// return a view
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No resource.");
			}
		}catch(IOException e){
			// call exception handler
		}
		
	}
}
