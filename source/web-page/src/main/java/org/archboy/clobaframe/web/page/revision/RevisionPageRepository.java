package org.archboy.clobaframe.web.page.revision;

import java.util.Locale;
import org.archboy.clobaframe.web.page.PageInfo;
import org.archboy.clobaframe.web.page.PageKey;
import org.archboy.clobaframe.web.page.PageRepository;

/**
 *
 * @author yang
 */
public interface RevisionPageRepository extends PageRepository {
	
	/**
	 * 
	 * @param pageKey
	 * @param revision
	 * @param title
	 * @param content
	 * @param urlName
	 * @param templateName
	 * @param authorName
	 * @param authorId
	 * @param comment
	 * @return 
	 */
	PageInfo save(PageKey pageKey, int revision,
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
