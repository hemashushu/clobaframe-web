package org.archboy.clobaframe.web.view.tool;

import java.util.Collection;
import java.util.List;

/**
 * Translate the web resource name into HTML page line.
 *
 * @author yang
 */
public interface WebResourceLocationHTMLTagWriter {

	/**
	 * 
	 * @param name
	 * @return such as <script src="..."></script>
	 */
	String write(String name);

	List<String> write(Collection<String> names);

}
