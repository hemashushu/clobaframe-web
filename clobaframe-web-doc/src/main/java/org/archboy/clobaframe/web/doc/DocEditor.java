package org.archboy.clobaframe.web.doc;

import java.util.Locale;

/**
 *
 * @author yang
 */
public interface DocEditor {
	
	Doc add(String name, String parentName,
		Locale locale,
		String title, String content, 
		String authorName, String authorId);
	
	void delete(String name, Locale locale);
	
	void delete(String name, Locale locale, int revision);
}
