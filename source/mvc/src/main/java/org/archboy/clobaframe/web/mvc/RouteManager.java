package org.archboy.clobaframe.web.mvc;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.ViewResolver;

/**
 *
 * @author yang
 */
public interface RouteManager {

	RouteDefinition get(HttpServletRequest request);
	
}
