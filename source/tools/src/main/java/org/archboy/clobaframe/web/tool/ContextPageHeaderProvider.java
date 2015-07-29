package org.archboy.clobaframe.web.tool;

import java.util.Map;

/**
 *
 * @author yang
 */
public interface ContextPageHeaderProvider extends PageHeaderProvider {

	/**
	 * Add a custom header to the current context HTTP request.
	 * 
	 * @param tagName
	 * @param attributes 
	 * @param closeTag 
	 */
	void add(String tagName, Map<String, Object> attributes, boolean closeTag);
	
}
