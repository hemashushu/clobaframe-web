package org.archboy.clobaframe.web.page.revision;

import java.util.Locale;
import org.archboy.clobaframe.web.page.PageRepository;

/**
 *
 * @author yang
 */
public interface RevisionPageRepository extends PageRepository {
	
	/**
	 * 
	 * @param name
	 * @param locale
	 * @return 0 if there is no active revision define.
	 */
	int getActiveRevision(String name, Locale locale);
	
	/**
	 * 
	 * @param name
	 * @param locale
	 * @param revision 
	 */
	void setActiveRevision(String name, Locale locale, int revision);
	
	/**
	 * 
	 * @param name
	 * @param locale
	 * @param revision 
	 */
	void delete(String name, Locale locale, int revision);
}
