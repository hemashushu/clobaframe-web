package org.archboy.clobaframe.web.page;

import java.util.Locale;

/**
 *
 * @author yang
 */
public interface PageRepository {
	
/**
	 * Add or update a page.
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
	
}
