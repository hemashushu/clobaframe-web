package org.archboy.clobaframe.web.doc;

import java.util.Collection;
import java.util.Locale;

/**
 *
 * @author yang
 */
public interface RevisionManager {
	
	Collection<Integer> list(String name, Locale locale);
	
	int get(String name, Locale locale);
	
	Doc getDoc(String name, Locale locale, int revision);

	void set(String name, Locale locale, int revision);
	
	void delete(String name, Locale locale, int revision);
}
