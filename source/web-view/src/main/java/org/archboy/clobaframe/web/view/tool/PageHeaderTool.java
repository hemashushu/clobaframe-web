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
	List<String> writeResources(Collection<String> resourceNames);

}
