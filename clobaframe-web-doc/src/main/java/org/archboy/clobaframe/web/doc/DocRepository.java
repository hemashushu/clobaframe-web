package org.archboy.clobaframe.web.doc;

import java.util.Collection;
import java.util.Locale;

/**
 *
 * @author yang
 */
public interface DocRepository {
	
	public static final int PRIORITY_TOP = 0;
	public static final int PRIORITY_LESS_THAN_TOP = 1;
	public static final int PRIORITY_DEFAULT = 5;
	
	/**
	 * Get the priority of repository.
	 * The high priority repository will be check first when get a resource by
	 * name.
	 * I.e. when there are resources that with duplicate name, the resource
	 * resist in the high priority repository will be selected.
	 * 
	 * @return 
	 */
	int getPriority();
	
	Doc get(String name, Locale locale);
	
	Doc get(String name, Locale locale, int revision);
	
	Collection<Doc> listLocale(String name);
}
