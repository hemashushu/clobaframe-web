package org.archboy.clobaframe.web.page.revision;

import java.util.Collection;
import java.util.Locale;
import org.archboy.clobaframe.web.page.PageKey;
import org.archboy.clobaframe.web.page.PageManager;

/**
 *
 * @author yang
 */
public interface RevisionPageManager extends PageManager {
	
	/**
	 * Get the specify page by the name and locale.
	 * 
	 * @param pageKey
	 * @param revision
	 * @return NULL if it does not found
	 */
	RevisionPage get(PageKey pageKey, int revision);
	
	/**
	 * 
	 * @param pageKey
	 * @return NULL if it does not found.
	 */
	Collection<RevisionPage> listRevision(PageKey pageKey);
	
	/**
	 * 
	 * @param pageKey
	 * @return 
	 */
	int getActiveRevision(PageKey pageKey);
	
	/**
	 * 
	 * @param pageKey
	 * @param revision 
	 */
	void setActiveRevision(PageKey pageKey, int revision);
	
	/**
	 *
	 * @param pageKey
	 * @param revision
	 */
	void delete(PageKey pageKey, int revision);
}
