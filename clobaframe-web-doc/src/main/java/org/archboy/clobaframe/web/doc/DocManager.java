package org.archboy.clobaframe.web.doc;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author yang
 */
public interface DocManager {
	
	Doc get(String name, Locale locale);
	
	Collection<Locale> listLocale(String name);

	Doc add(String name, String parentName,
		Locale locale,
		String title, String content, 
		String authorName, String authorId);
	
	void delete(String name, Locale locale);
	
}
