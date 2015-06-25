package org.archboy.clobaframe.web.view.tool;

import java.util.List;
import java.util.Map;

/**
 *
 * @author yang
 */
public interface PageHeaderExtensionTool {
	
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

	/**
	 * Get page headers in the current HTTP request.
	 * 
	 * Usually these page headers are exists in all pages and provide by
	 * {@link PageHeaderProvider}, but
	 * the custom header that added by {@link PageHeaderTool#addHeader} 
	 * is also included.
	 * 
	 * @return EMPTY if no extra page header.
	 */
	List<String> getHeaders();
	
	/**
	 * 
	 * @return EMPTY string if no extra page header.
	 */
	String write();
	
	/**
	 * 
	 * @param seperator
	 * @return EMPTY string if no extra page header.
	 */
	String write(String seperator);
}
