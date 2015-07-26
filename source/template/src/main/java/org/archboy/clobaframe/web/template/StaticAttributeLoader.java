package org.archboy.clobaframe.web.template;

import java.util.Map;

/**
 *
 * Static attributes loader.
 * 
 * Static attribute is the object that put to view render, such as
 * i18n tool, escape tool etc.
 * 
 * Commonly the name is the bean name and the object is the bean instance that from
 * BeanFactory.
 * 
 * @author yang
 */
public interface StaticAttributeLoader {
	
	Map<String, Object> list();
	
}
