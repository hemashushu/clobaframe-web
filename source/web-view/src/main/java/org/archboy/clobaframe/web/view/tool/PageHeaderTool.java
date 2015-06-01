package org.archboy.clobaframe.web.view.tool;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author yang
 */
public interface PageHeaderTool {

	/**
	 * Write a header with tag and attributes specify.
	 * 
	 * Consider this:
	 * &lt;link href="/atom.xml" 
	 *	type="application/atom+xml" 
	 *	rel="alternate" 
	 *	title="RSS Feed" &gt;
	 * 
	 * The "link" is tag name, and "href='/atom.xml'", "rel='alternate'" are attributes.
	 * 
	 * @param tagName
	 * @param attributes
	 * @param closeTag
	 * @return 
	 */
	String write(String tagName, Map<String, Object> attributes, boolean closeTag);
	
	/**
	 * Write a resource page header line.
	 * 
	 * The resource type is automatically detected, current supports 
	 * &lt;script src="xxx"&gt; for javascript and 
	 * &lt;link href="xxx" rel="stylesheet"&gt; for stylesheet.
	 * 
	 * @param resourceName
	 * @return 
	 */
	String writeResource(String resourceName);
	
	/**
	 * Write a resource page header line with custom tag name and 
	 * location attribute name and extra attributes.
	 * 
	 * @param resourceName
	 * @param tagName
	 * @param locationAttributeName
	 * @param otherAttributes
	 * @param closeTag
	 * @return 
	 */
	String writeResource(String resourceName, 
			String tagName, 
			String locationAttributeName, 
			Map<String, Object> otherAttributes, boolean closeTag);
	
	/**
	 * Get serval resource page header lines.
	 * 
	 * @param resourceNames
	 * @return 
	 */
	List<String> getResources(Collection<String> resourceNames);
	
	/**
	 * Add a custom header to the current HTTP request.
	 * 
	 * The custom headers will write into the current HTTP request attributes.
	 * 
	 * @param tagName
	 * @param attributes 
	 * @param closeTag 
	 */
	void addHeader(String tagName, Map<String, Object> attributes, boolean closeTag);

	/**
	 * Get page headers in the current HTTP request.
	 * 
	 * Usually these page headers are exists in all pages and provide by
	 * {@link PageHeaderProvider}, but
	 * the custom header that added by {@link PageHeaderTool#addHeader} 
	 * is also included.
	 * 
	 * @return 
	 */
	List<String> getHeaders();
}
