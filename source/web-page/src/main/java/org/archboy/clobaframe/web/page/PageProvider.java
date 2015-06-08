package org.archboy.clobaframe.web.page;

import java.util.Collection;
import java.util.Locale;
import org.springframework.core.Ordered;

/**
 *
 * @author yang
 */
public interface PageProvider extends Ordered{
	
	public static final int PRIORITY_HIGHEST = 0;
	public static final int PRIORITY_HIGHER = 20;
	public static final int PRIORITY_HIGH = 40;
	public static final int PRIORITY_NORMAL = 60;
	public static final int PRIORITY_LOW = 80;
	public static final int PRIORITY_LOWER = 100;
	
	/**
	 * Get the priority of the current provider.
	 * The higher priority provider will be check first when get a page by name.
	 * I.E. when there are several pages that with the same name and locale, the page
	 * that resist in the higher priority provider will be selected.
	 * 
	 * @return 
	 */
	//int getOrder();
	
	/**
	 * 
	 * @return 
	 */
	Collection<PageInfo> getAll();
}
