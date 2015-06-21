package org.archboy.clobaframe.web.theme;

import java.util.Collection;

/**
 *
 * @author yang
 */
public interface ThemeProvider {
	
	Collection<ThemePackage> getPackages();
	
	ThemePackage get(String catalog, String name);
	
}
