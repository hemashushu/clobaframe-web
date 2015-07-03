package org.archboy.clobaframe.web.theme;


import org.archboy.clobaframe.web.theme.ThemePackage;
import org.archboy.clobaframe.webresource.WebResourceInfo;

/**
 *
 * @author yang
 */
public interface ThemeRepository {
	
	ThemePackage create(String catalog, String id);
	
	ThemePackage update(ThemePackage themePackage, 
			String name,
			String description, String version,
			String authorName, String website);
	
	void delete(ThemePackage themePackage);
	
	void save(ThemePackage themePackage, ThemeResourceInfo themeResourceInfo);
	
	void delete(ThemePackage themePackage, String id);
	
}
