package org.archboy.clobaframe.web.page.revision;

import java.util.Collection;
import java.util.Locale;
import org.archboy.clobaframe.web.page.PageManager;

/**
 *
 * @author yang
 */
public interface RevisionPageManager extends PageManager {
	
	/**
	 * Get the specify page by the name and locale.
	 * 
	 * @param name
	 * @param locale
	 * @param revision
	 * @return NULL if it does not found
	 */
	RevisionPage get(String name, Locale locale, int revision);
	
	/**
	 * 
	 * @param name
	 * @param locale
	 * @return NULL if it does not found.
	 */
	Collection<RevisionPage> listRevision(String name, Locale locale);
	
	/**
	 * 
	 * @param name
	 * @param locale
	 * @return 
	 */
	int getActiveRevision(String name, Locale locale);
	
	/**
	 * 
	 * @param name
	 * @param locale
	 * @param revision 
	 */
	void setActiveRevision(String name, Locale locale, int revision);
	
	void delete(String name, Locale locale, int revision);
}
