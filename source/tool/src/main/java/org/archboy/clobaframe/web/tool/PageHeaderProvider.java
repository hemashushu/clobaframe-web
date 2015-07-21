package org.archboy.clobaframe.web.tool;

import java.util.Collection;

/**
 * Add extra page header to {@link PageHeaderExtensionTool}.
 * 
 * @author yang
 */
public interface PageHeaderProvider {
	
	/**
	 * Provider name.
	 * It's optional.
	 * 
	 * @return 
	 */
	String getName();
	
	/**
	 * 
	 * @return 
	 */
	Collection<String> list();
}
