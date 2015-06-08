package org.archboy.clobaframe.web.theme;


import org.archboy.clobaframe.web.theme.ThemeInfo;
import org.archboy.clobaframe.webresource.WebResourceInfo;

/**
 *
 * @author yang
 */
public interface ThemeRepository {
	
	ThemeInfo create(String name, String description);
	
	ThemeInfo update(ThemeInfo themeInfo, 
			String description, String version,
			String authorName, String website);
	
	void delete(ThemeInfo themeInfo);
	
	void save(ThemeInfo themeInfo, ThemeInfo.ResourceType resourceType, WebResourceInfo webResourceInfo);
	
	void delete(ThemeInfo themeInfo, ThemeInfo.ResourceType resourceType, String name);
	
}
