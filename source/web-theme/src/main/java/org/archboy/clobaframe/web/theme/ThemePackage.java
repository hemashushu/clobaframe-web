package org.archboy.clobaframe.web.theme;

import java.util.Collection;
import java.util.Date;
import org.archboy.clobaframe.webresource.WebResourceInfo;

/**
 *
 * @author yang
 */
public interface ThemePackage {
	
//	public static enum ResourceType {
//		template,
//		resource
//	}
	
	String getName();
	
	String getCatalog();
	
	String getDescription();
	
	String getVersion();
	
	Date getLastModified();
	
	String getAuthorName();
	
	String getWebsite();
	
	boolean isReadOnly();
	
	Collection<ThemeResourceInfo> getResources();
	
	ThemeResourceInfo getResource(String name);
	
}
