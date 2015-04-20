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
	 * 
	 * @param pageHeaderProvider 
	 */
	void addPageHeaderProvider(PageHeaderProvider pageHeaderProvider);
	
	/**
	 * Get the page headers.
	 * 
	 * Usually these page headers are exists in all pages, but
	 * the custom header that added by {@link PageHeaderTool#addHeader} 
	 * will also be included.
	 * 
	 * @return 
	 */
	List<String> getHeaders();
	
	/**
	 * Add a custom header to the current HTTP request scope.
	 * 
	 * The custom headers are write into the current HTTP request attributes.
	 * 
	 * @param tagName
	 * @param attributes 
	 * @param closeTag 
	 */
	void addHeader(String tagName, Map<String, Object> attributes, boolean closeTag);
	
	
	/**
	 * Write header by tag and attributes.
	 * 
	 * Consider this:
	 * &lt;link href="/atom.xml" 
	 *	type="application/atom+xml" 
	 *	rel="alternate" 
	 *	title="RSS Feed" &gt;
	 * 
	 * The "link" is tag, and "href='/atom.xml'", "rel='alternate'" are attributes.
	 * 
	 * @param tagName
	 * @param attributes
	 * @param closeTag
	 * @return 
	 */
	String writeHeader(String tagName, Map<String, Object> attributes, boolean closeTag);
	
	/**
	 * Write a resource page header line.
	 * 
	 * Includes &lt;script src="xxx"&gt; for javascript and 
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
	String writeResource(String tagName, 
			String locationAttributeName, String resourceName,
			Map<String, Object> otherAttributes, boolean closeTag);
	
	/**
	 * Get serval resource page header lines.
	 * 
	 * @param resourceNames
	 * @return 
	 */
	List<String> getResources(Collection<String> resourceNames);
}
