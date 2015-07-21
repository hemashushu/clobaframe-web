package org.archboy.clobaframe.web.page.revision;

import java.util.Collection;
import java.util.Locale;
import org.archboy.clobaframe.web.page.PageKey;
import org.archboy.clobaframe.web.page.PageManager;

/**
 * Page manager that supports revision.
 * Please note that the {@link #save()} method always create a page or a new revision.
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
	RevisionPageInfo get(PageKey pageKey, int revision);
	
	/**
	 * 
	 * @param pageKey
	 * @return EMPTY when no revisions.
	 */
	Collection<RevisionPageInfo> listRevision(PageKey pageKey);
	
//	/**
//	 * Get the active revision.
//	 * Normally the active revision is the latest revision number.
//	 * 
//	 * @param pageKey
//	 * @return 0 when there is no other revision. -1 when there is no the specify key.
//	 */
//	int getCurrentRevision(PageKey pageKey);
	
	/**
	 * Rollback a revision.
	 * Normally the target revision will turn into the new latest revision,
	 * and the old revision will be deleted.
	 * @param pageKey
	 * @param revision 
	 * @return  
	 */
	RevisionPageInfo rollbackRevision(PageKey pageKey, int revision);
	
	/**
	 *
	 * @param pageKey
	 * @param revision
	 */
	void delete(PageKey pageKey, int revision);
}
