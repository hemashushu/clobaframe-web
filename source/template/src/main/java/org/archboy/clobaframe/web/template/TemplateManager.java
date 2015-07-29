package org.archboy.clobaframe.web.template;

import org.archboy.clobaframe.io.NamedResourceInfo;

/**
 *
 * @author yang
 */
public interface TemplateManager {
	
	/**
	 * 
	 * @param name
	 * @return NULL if the specify resource not found.
	 */
	NamedResourceInfo get(String name);
	
}
