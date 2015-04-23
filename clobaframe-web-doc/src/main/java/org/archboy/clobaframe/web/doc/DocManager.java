package org.archboy.clobaframe.web.doc;

import java.util.Collection;
import java.util.Locale;

/**
 * Site doc manager.
 * 
 * A doc manager will includes several providers and one repository. 
 * 
 * @author yang
 */
public interface DocManager {
	
	/**
	 * Get the specify doc by the name and locale.
	 * 
	 * @param name
	 * @param locale
	 * @return NULL if it does not found.
	 */
	Doc get(String name, Locale locale);
	
	/**
	 * List all available locale for the specify doc.
	 * 
	 * @param name
	 * @return NULL if it does not found.
	 */
	Collection<Locale> listLocale(String name);

	/**
	 * Add or update a doc.
	 * 
	 * @param name
	 * @param parentName NULL for the top most level doc.
	 * @param locale
	 * @param title
	 * @param content
	 * @param templateName Optional
	 * @param authorName Optional.
	 * @param authorId Optional.
	 * @param updateNote Optional
	 * @return 
	 * @throws IllegalArgumentException if parent doc does not found.
	 */
	Doc save(String name, String parentName,
		Locale locale,
		String title, String content, 
		String templateName,
		String authorName, String authorId, String updateNote);
	
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
