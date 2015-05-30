package org.archboy.clobaframe.web.page;

import java.util.Collection;
import java.util.Locale;

/**
 * Site doc manager.
 * 
 * A doc manager will includes several providers and one repository. 
 * 
 * @author yang
 */
public interface PageManager {
	
	/**
	 * Get the specify page by the name and locale.
	 * 
	 * @param pageKey
	 * @return NULL if it does not found.
	 */
	Page get(PageKey pageKey);
	
	/**
	 * Get the page name by the URL name.
	 * 
	 * @param urlName
	 * @return 
	 */
	String getName(String urlName);
	
	/**
	 * List all available locale for the specify page.
	 * 
	 * @param name
	 * @return NULL if it does not found.
	 */
	Collection<Locale> listLocale(String name);

	/**
	 * Add or update a doc.
	 * 
	 * @param pageKey
	 * @param title
	 * @param content
	 * @param urlName Optional.
	 * @param templateName Optional.
	 * @param authorName Optional.
	 * @param authorId Optional.
	 * @param updateNote Optional.
	 * @return 
	 * @throws IllegalArgumentException if parent doc does not found.
	 */
	Page update(PageKey pageKey,
		String title, String content, 
		String urlName, String templateName,
		String authorName, String authorId, String updateNote);
	
	/**
	 * Delete a page.
	 * 
	 * It will NOT raise an exception if the specify page does not found.
	 * 
	 * @param pageKey
	 */
	void delete(PageKey pageKey);
	
	Locale getDefaultLocale();
}
