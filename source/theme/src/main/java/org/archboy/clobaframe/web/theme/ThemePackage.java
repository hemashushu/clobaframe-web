package org.archboy.clobaframe.web.theme;

import java.util.Collection;
import java.util.Date;

/**
 *
 * @author yang
 */
public interface ThemePackage {

	/**
	 * The theme catalog.
	 * Typically there are two catalogs: application global theme and user theme.
	 * 
	 * @return 
	 */
	String getCatalog();
	
	/**
	 * The theme id must be unique.
	 * @return 
	 */
	String getId();

	/**
	 * For display and human reading.
	 * 
	 * @return 
	 */
	String getName();
	
	String getDescription();
	
	String getVersion();
	
	Date getLastModified();
	
	String getAuthorName();
	
	String getWebsite();
	
	boolean isReadOnly();
	
	Collection<ThemeResourceInfo> listResource();
	
	ThemeResourceInfo getResource(String name);
	
}
