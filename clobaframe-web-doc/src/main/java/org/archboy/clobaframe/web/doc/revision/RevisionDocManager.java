package org.archboy.clobaframe.web.doc.revision;

import java.util.Collection;
import java.util.Locale;
import org.archboy.clobaframe.web.doc.DocManager;

/**
 *
 * @author yang
 */
public interface RevisionDocManager extends DocManager {
	
	/**
	 * 
	 * @param name
	 * @param locale
	 * @param revision
	 * @return NULL if it does not found
	 */
	RevisionDoc get(String name, Locale locale, int revision);
	
	/**
	 * 
	 * @param name
	 * @param locale
	 * @return NULL if it does not found.
	 */
	Collection<RevisionDoc> listRevision(String name, Locale locale);
	
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
	
	void delete(String name, Locale locale, int revision);
}
