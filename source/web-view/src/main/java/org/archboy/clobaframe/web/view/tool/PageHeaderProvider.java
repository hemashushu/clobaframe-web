package org.archboy.clobaframe.web.view.tool;

import java.util.List;

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
	List<String> list();
}
