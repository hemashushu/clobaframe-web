package org.archboy.clobaframe.web.doc;

import java.util.Locale;

/**
 *
 * @author yang
 */
public interface DocRepository {
	
	/**
	 * Add or update a doc.
	 * 
	 * @param name
	 * @param parentName
	 * @param locale
	 * @param title
	 * @param content
	 * @param authorName
	 * @param authorId
	 * @return 
	 * @throws IllegalArgumentException if the parent doc does not found.
	 */
	Doc save(String name, String parentName,
		Locale locale,
		String title, String content, 
		String authorName, String authorId);
	
	/**
	 * Delete a doc.
	 * 
	 * It will NOT raise an exception if the specify doc does not found.
	 * 
	 * @param name
	 * @param locale 
	 */
	void delete(String name, Locale locale);
	
}
