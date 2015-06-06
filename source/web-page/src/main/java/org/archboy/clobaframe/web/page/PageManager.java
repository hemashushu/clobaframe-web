package org.archboy.clobaframe.web.page;

import java.util.Collection;
import java.util.Locale;

/**
 * Page manager.
 * 
 * Page manager includes several providers and one repository. 
 * 
 * @author yang
 */
public interface PageManager {
	
	/**
	 * List all available locale for the specify page.
	 * 
	 * @param name
	 * @return NULL when the page name does not exist.
	 */
	Collection<Locale> listLocale(String name);
	
	/**
	 * Get the specify page by the key (page name and locale).
	 * 
	 * @param pageKey
	 * @return NULL when the page key does not exist.
	 */
	Page get(PageKey pageKey);
	
	/**
	 * Get the page name by the URL name.
	 * 
	 * @param urlName
	 * @return NULL when the specify URL name page does not exist.
	 */
	String getByUrlName(String urlName);

	/**
	 * Create or update a page.
	 * In some extend manager that supports the revision, this method
	 * always create new page or new revision, it would not update a page or
	 * a revision.
	 * 
	 * @param pageKey
	 * @param title
	 * @param content
	 * @param urlName Optional.
	 * @param templateName Optional.
	 * @param authorName Optional.
	 * @param authorId Optional.
	 * @param comment Optional.
	 * @return 
	 */
	Page save(PageKey pageKey,
		String title, String content, 
		String urlName, String templateName,
		String authorName, String authorId, String comment);

	/**
	 * 
	 * @param name 
	 */
	void delete(String name);
	
	/**
	 * Delete a specify locale page.
	 * 
	 * When the specify page does not found, it will NOT occurs an exception.
	 * 
	 * @param pageKey
	 */
	void delete(PageKey pageKey);
	
	/**
	 * The application default locale.
	 * Commonly it's the 'en' or 'en_US'.
	 * @return 
	 */
	Locale getDefaultLocale();
}
