package org.archboy.clobaframe.web.page.revision;

import java.util.Locale;
import org.archboy.clobaframe.web.page.PageKey;
import org.archboy.clobaframe.web.page.PageRepository;

/**
 *
 * @author yang
 */
public interface RevisionPageRepository extends PageRepository {
	
	/**
	 * Get the active revision.
	 * Normally the active revision is the latest revision number.
	 * 
	 * @param pageKey
	 * @return 0 when there is no other revision.
	 */
	int getActiveRevision(PageKey pageKey);
	
	/**
	 * 
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
