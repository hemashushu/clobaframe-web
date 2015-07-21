package org.archboy.clobaframe.web.view.tool.context;

import java.util.List;
import java.util.Map;

/**
 *
 * @author yang
 */
public interface ContextPageHeaderTool {
	
	/**
	 * 
	 * @param pageHeaderProvider 
	 */
	void addProvider(ContextPageHeaderProvider pageHeaderProvider);
	
	/**
	 * 
	 * @param providerName 
	 */
	void removeProvider(String providerName);
	
	/**
	 * Get page headers in the current HTTP request.
	 * 
	 * Usually these page headers are exists in all pages and provide by
	 * {@link ContextPageHeaderProvider}, but
	 * the custom header that added by {@link PageHeaderTool#addHeader} 
	 * is also included.
	 * 
	 * @return EMPTY if no extra page header.
	 */
	List<String> list();
	
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
