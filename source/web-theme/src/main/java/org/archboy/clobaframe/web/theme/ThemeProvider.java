package org.archboy.clobaframe.web.theme;

import java.util.Collection;

/**
 *
 * @author yang
 */
public interface ThemeProvider {
	
	Collection<String> getThemes();
	
	ThemeInfo get(String name);
	
	
	
}
