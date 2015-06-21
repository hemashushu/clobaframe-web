package org.archboy.clobaframe.web.theme;

import java.util.Collection;
import java.util.Date;
import org.archboy.clobaframe.webresource.WebResourceInfo;

/**
 *
 * @author yang
 */
public interface ThemeManager {
	
	public static final String PACKAGE_CATALOG_BASE = "base";
	public static final String PACKAGE_CATALOG_LOCAL = "local";
	
	public static final String PACKAGE_NAME_BASE = "base";
	
	public static final String MIME_TYPE_VELOCITY_TEMPLATE = "text/x-velocity";
	
	Collection<ThemePackage> list(String catalog);
	
	ThemePackage get(String catalog, String name);
	
	ThemePackage create(String catalog, String name);
	
	ThemePackage update(ThemePackage themePackage, 
			String name,
			String description, String version,
			String authorName, String website);
	
	ThemePackage clone(ThemePackage themePackage, boolean includeTemplate, String catalog, String name);
	
	void save(ThemePackage themePackage, ThemeResourceInfo themeResourceInfo);
	
	void delete(ThemePackage themePackage, String name);
	
	void delete(ThemePackage themePackage);

}
