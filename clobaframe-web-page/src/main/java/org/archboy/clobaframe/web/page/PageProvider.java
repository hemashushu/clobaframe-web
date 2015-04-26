package org.archboy.clobaframe.web.page;

import java.util.Collection;
import java.util.Locale;

/**
 *
 * @author yang
 */
public interface PageProvider {
	
	public static final int PRIORITY_HIGHEST = 0;
	public static final int PRIORITY_HIGHER = 2;
	public static final int PRIORITY_HIGH = 4;
	public static final int PRIORITY_NORMAL = 5;
	
	/**
	 * Get the priority of the current provider.
	 * The higher priority provider will be check first when get a page by name.
	 * I.E. when there are several pages that with the same name and locale, the page
	 * that resist in the higher priority provider will be selected.
	 * 
	 * @return 
	 */
	int getPriority();
	
	/**
	 * 
	 * @return 
	 */
	Collection<Page> getAll();
}
