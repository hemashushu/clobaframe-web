package org.archboy.clobaframe.web.page;

/**
 *
 * @author yang
 */
public interface PageRepository {
	
/**
	 * Add or update a page.
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
	Page update(PageKey pageKey,
		String title, String content, 
		String urlName, String templateName,
		String authorName, String authorId, String comment);
	
	/**
	 * Delete a page.
	 * 
	 * It will NOT raise an exception when the specify page does not found.
	 * 
	 * @param pageKey
	 */
	void delete(PageKey pageKey);
	
}
