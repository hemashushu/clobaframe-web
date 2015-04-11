package org.archboy.clobaframe.web.view.tool;

import java.util.List;
import java.util.Map;

/**
 *
 * @author yang
 */
public interface PageHeaderTool {
	
	/**
	 * 
	 * @param pageHeaderProvider 
	 */
	void addPageHeaderProvider(PageHeaderProvider pageHeaderProvider);
	
	/**
	 * 
	 * @return 
	 */
	List<String> getHeaders();
	
	/**
	 * Add a custom header.
	 * The custom headers are write into the current HTTP request attributes.
	 * @param tag
	 * @param attributes 
	 */
	void addHeader(String tag, Map<String, String> attributes);
	
	
	/**
	 * Write header by tag and attributes.
	 * 
	 * Consider this:
	 * &lt;link href="/atom.xml" 
	 *	type="application/atom+xml" 
	 *	rel="alternate" 
	 *	title="RSS Feed" &gt;
	 * 
	 * The "link" is tag, and "href='/atom.xml'" is attribute.
	 * 
	 * @param tag
	 * @param attributes
	 * @return 
	 */
	String writeHeader(String tag, Map<String, String> attributes);
}
