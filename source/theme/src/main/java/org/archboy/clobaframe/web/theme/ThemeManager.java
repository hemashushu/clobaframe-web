package org.archboy.clobaframe.web.theme;

import java.util.Collection;

/**
 *
 * @author yang
 */
public interface ThemeManager {
	
	public static final String PACKAGE_CATALOG_BASE = "base";
	public static final String PACKAGE_CATALOG_LOCAL = "local";
	
	public static final String PACKAGE_ID_BASE = "base";
	
	public static final String MIME_TYPE_VELOCITY_TEMPLATE = "text/x-velocity";
	
	Collection<ThemePackage> list(String catalog);
	
	ThemePackage get(String catalog, String id);
	
	ThemePackage create(String catalog, String id);
	
	ThemePackage update(ThemePackage themePackage, 
			String name,
			String description, String version,
			String authorName, String website);
	
	ThemePackage clone(ThemePackage themePackage, int contentTypes, String catalog, String id);
	
	void save(ThemePackage themePackage, ThemeResourceInfo themeResourceInfo);
	
	void delete(ThemePackage themePackage, String id);
	
	void delete(ThemePackage themePackage);

}
