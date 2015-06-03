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
	 * To get the active revision, use the {@link PageManager#get(PageKey)} method.
	 * 
	 * @param pageKey
	 * @param revision
	 * @return NULL when it does not found
	 */
	RevisionPage get(PageKey pageKey, int revision);
	
	/**
	 * 
	 * @param pageKey
	 * @return 
	 */
	Collection<RevisionPage> listRevision(PageKey pageKey);
	
	/**
	 * Get the active revision.
	 * Normally the active revision is the latest revision number.
	 * 
	 * @param pageKey
	 * @return 0 when there is no other revision.
	 */
	int getCurrentRevision(PageKey pageKey);
	
	/**
	 * Rollback a revision.
	 * Normally the target revision will turn into the new latest revision,
	 * and the old revision will be deleted.
	 * @param pageKey
	 * @param revision 
	 */
	void rollbackRevision(PageKey pageKey, int revision);
	
	/**
	 *
	 * @param pageKey
	 * @param revision
	 */
	void delete(PageKey pageKey, int revision);
}
