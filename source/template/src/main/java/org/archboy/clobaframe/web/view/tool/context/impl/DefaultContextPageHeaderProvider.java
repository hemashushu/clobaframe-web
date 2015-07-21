package org.archboy.clobaframe.web.view.tool.context.impl;

import java.util.List;
import java.util.Map;

/**
 *
 * @author yang
 */
public interface DefaultContextPageHeaderProvider {

	/**
	 * Add a custom header to the current HTTP request.
	 * 
	 * The custom headers will write into the current HTTP request attributes.
	 * 
	 * @param tagName
	 * @param attributes 
	 * @param closeTag 
	 */
	void add(String tagName, Map<String, Object> attributes, boolean closeTag);
	
}
