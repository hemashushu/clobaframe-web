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
	 * List all available locale for the specify page.
	 * 
	 * @param name
	 * @return EMPTY when the page name does not exist.
	 */
	Collection<Locale> listLocale(String name);
	
	/**
	 * Get the specify page by the key (page name and locale).
	 * 
	 * @param pageKey
	 * @return NULL when the page key does not exist.
	 */
	PageInfo get(PageKey pageKey);
	
	/**
	 * Get the page name by the URL name.
	 * 
	 * Note: All pages with the same page name SHOULD be the same URL name.
	 * 
	 * @param urlName
	 * @return NULL when the specify URL name page does not exist.
	 */
	String getByUrlName(String urlName);

	/**
	 * 
	 * @param locale
	 * @return EMPTY when the specify locale has no page.
	 */
	Collection<PageInfo> listByLocale(Locale locale);
	
	/**
	 * 
	 * @return EMPTY when no page.
	 */
	Collection<PageInfo> list();
}
