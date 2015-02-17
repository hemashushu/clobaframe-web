package org.archboy.clobaframe.web.view.tool;

import java.util.Collection;
import java.util.List;

/**
 * Translate the web resource name into HTML page line.
 *
 * @author yang
 */
public interface PageWebResourceHeaderTool {

	String getHeader(String resourceName);
	List<String> getHeaders(Collection<String> resourceNames);

}
