package org.archboy.clobaframe.web.doc.revision;

import java.util.Locale;
import org.archboy.clobaframe.web.doc.DocRepository;

/**
 *
 * @author yang
 */
public interface RevisionDocRepository extends DocRepository {
	
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
	
	/**
	 * 
	 * @param name
	 * @param locale
	 * @param revision 
	 */
	void delete(String name, Locale locale, int revision);
}
