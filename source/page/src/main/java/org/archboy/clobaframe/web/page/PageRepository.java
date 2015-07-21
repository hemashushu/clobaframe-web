package org.archboy.clobaframe.web.page;

/**
 *
 * @author yang
 */
public interface PageRepository {
	
	/**
	 * Create or update a page.
	 * In some extend manager that supports the revision, this method
	 * always create new page or new revision, it would not update a page or
	 * a revision.
	 * 
	 * @param pageKey
	 * @param title
	 * @param content
	 * @param urlName Optional.
	 * @param templateName Optional.
	 * @param authorName Optional.
	 * @param authorId Optional.
	 * @param comment Optional.
	 * @return 
	 */
	PageInfo save(PageKey pageKey,
		String title, String content, 
		String urlName, String templateName,
		String authorName, String authorId, String comment);
	
	/**
	 * Delete page by name.
	 * 
	 * @param name 
	 */
	void delete(String name);
	
	/**
	 * Delete a specify locale page.
	 * 
	 * It will NOT raise an exception when the specify page does not found.
	 * 
	 * @param pageKey
	 */
	void delete(PageKey pageKey);
	
}
