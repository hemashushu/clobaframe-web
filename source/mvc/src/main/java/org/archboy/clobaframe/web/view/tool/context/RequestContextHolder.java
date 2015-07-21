package org.archboy.clobaframe.web.view.tool.context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author yang
 */
public interface RequestContextHolder {
	
	void clear();
	
	RequestContext get();
	
	void set(RequestContext requestContext);
	
}
