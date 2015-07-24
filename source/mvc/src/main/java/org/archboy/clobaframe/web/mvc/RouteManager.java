package org.archboy.clobaframe.web.mvc;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Route mapping manager.
 * 
 * Note:
 * Currently clobaframe mvc does not support {@link RequestMapping} params/headers/consumes
 * filter, and the URL path pattern use Regex instead Spring Ant style pattern.
 * 
 * @author yang
 */
public interface RouteManager {

	List<RouteDefinition> list();
	
}
