package org.archboy.clobaframe.web.theme;

import java.util.Date;
import org.archboy.clobaframe.webresource.WebResourceInfo;

/**
 *
 * @author yang
 */
public interface ThemeManager {
	
	ThemeInfo get(String name);
	
	ThemeInfo create(String name, String description);
	
	ThemeInfo update(ThemeInfo themeInfo, 
			String description, String version,
			String authorName, String website);
	
	ThemeInfo clone(ThemeInfo themeInfo, String name);
	
	void save(ThemeInfo themeInfo, ThemeInfo.ResourceType resourceType, WebResourceInfo webResourceInfo);
	
	void delete(ThemeInfo themeInfo, ThemeInfo.ResourceType resourceType, String name);
	
	void delete(ThemeInfo themeInfo);

}
