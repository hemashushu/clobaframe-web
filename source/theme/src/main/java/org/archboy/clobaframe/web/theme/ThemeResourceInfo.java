package org.archboy.clobaframe.web.theme;

import org.archboy.clobaframe.io.NamedResourceInfo;

/**
 * The ThemeResourceInfo extends interface NamedResourceInfo.
 * 
 * Keep in mind the theme resource name does NOT always equals web resource name.
 * By default, the base theme resource name is "resource/(css|js|image)/NAME.ext" and
 * "template/NAME.ext" for template, but the relate web resource name is "(css|js|image)/NAME.ext" and 
 * there is no template web resource.
 * 
 * Besides the base theme package, the local theme resource name is "resource/
 * (css|js|image)/NAME.ext" and "template/NAME.ext" for template. When they exports to web URL, it
 * will be "theme/THEME-NAME/resource/(css|js|image)/NAME.ext" and of cause
 * there is no template web resource.
 * 
 * @author yang
 */
public interface ThemeResourceInfo extends NamedResourceInfo {
	
	public static final int TYPE_RESOURCE = 1;
	public static final int TYPE_TEMPLATE = 2;
	
	//boolean isTemplate();
	int getContentType();
	
}
