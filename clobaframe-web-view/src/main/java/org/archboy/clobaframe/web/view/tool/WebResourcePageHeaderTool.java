package org.archboy.clobaframe.web.view.tool;

import java.util.Collection;
import java.util.List;

/**
 * Convert the web resource name into HTML page line.
 *
 * @author yang
 */
public interface WebResourcePageHeaderTool {

	/**
	 * Get the page header line 
	 * &lt;script src="xxx"&gt; for javascript and 
	 * &lt;link href="xxx" rel="stylesheet"&gt; or stylesheet.
	 * 
	 * @param resourceName
	 * @return 
	 */
	String writeHeader(String resourceName);
	
	/**
	 * 
	 * @param resourceNames
	 * @return 
	 */
	List<String> getHeaders(Collection<String> resourceNames);

}
