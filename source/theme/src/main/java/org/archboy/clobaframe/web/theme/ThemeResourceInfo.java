package org.archboy.clobaframe.web.theme;

import org.archboy.clobaframe.io.NamedResourceInfo;

/**
 * The ThemeResourceInfo extends interface NamedResourceInfo.
 * 
 * Keep in mind the theme resource name does NOT always equals web resource name.
 * By default, the base theme resource name is "resource/(css|js|image)/NAME.ext" or
 * "template/NAME.ext", but the relate web resource name is "(css|js|image)/NAME.ext" and 
 * there is no template web resource.
 * 
 * Besides the base theme package, the local theme resource name is "theme/THEME-NAME/resource/
 * (css|js|image)/NAME.ext" or "theme/THEME-NAME/template/NAME.ext", all they are equals the 
 * web resource name.
 * 
 * @author yang
 */
public interface ThemeResourceInfo extends NamedResourceInfo {
	
	boolean isTemplate();
	
}
