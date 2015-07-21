package org.archboy.clobaframe.web.view.tool.context;

import java.util.List;

/**
 * Add extra page header to {@link PageHeaderExtensionTool}.
 * 
 * @author yang
 */
public interface ContextPageHeaderProvider {
	
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
