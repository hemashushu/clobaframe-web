package org.archboy.clobaframe.web.view.tool;

import java.util.List;

/**
 * Add extra page header to {@link PageHeaderExtensionTool}.
 * 
 * @author yang
 */
public interface PageHeaderProvider {
	
	/**
	 * 
	 * @return 
	 */
	List<String> list();
}
