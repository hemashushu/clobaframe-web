package org.archboy.clobaframe.web.theme;

import java.util.Collection;
import java.util.Date;
import org.archboy.clobaframe.webresource.WebResourceInfo;

/**
 *
 * @author yang
 */
public interface ThemeInfo {
	
	public static enum ResourceType {
		template,
		resource
	}
	
	String getName();
	
	String getDescription();
	
	String getVersion();
	
	Date getLastModified();
	
	String getAuthorName();
	
	String getWebsite();
	
	Collection<WebResourceInfo> getTemplate();
	
	WebResourceInfo getTemplate(String name);
	
	Collection<WebResourceInfo> getResources();
	
	WebResourceInfo getResources(String name);
	
}
