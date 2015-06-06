package org.archboy.clobaframe.web.page.revision;

import java.util.Locale;
import org.archboy.clobaframe.web.page.Page;
import org.archboy.clobaframe.web.page.PageKey;
import org.archboy.clobaframe.web.page.PageRepository;

/**
 *
 * @author yang
 */
public interface RevisionPageRepository extends PageRepository {
	
	/**
	 * TODO::// DELETE THIS METHOD, REPLACE WITH THE ALTERNATIVE save(int revision, ...).
	 * @param pageKey
	 * @param revision 
	 * @return  
	 */
	//RevisionPage rollbackRevision(PageKey pageKey, int revision);
	
	Page save(PageKey pageKey, int revision,
		String title, String content, 
		String urlName, String templateName,
		String authorName, String authorId, String comment);
	
	/**
	 * 
	 * @param pageKey
	 * @param revision 
	 */
	void delete(PageKey pageKey, int revision);
}
