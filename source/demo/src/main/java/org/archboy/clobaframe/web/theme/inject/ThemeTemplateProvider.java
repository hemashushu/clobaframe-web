package org.archboy.clobaframe.web.theme.inject;

import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.io.NamedResourceInfo;
import org.archboy.clobaframe.setting.global.GlobalSetting;
import org.archboy.clobaframe.web.template.TemplateProvider;
import org.archboy.clobaframe.web.theme.ThemeManager;
import org.archboy.clobaframe.web.theme.ThemePackage;

/**
 * Inject (append or replace exists) theme template into template manager.
 * @author yang
 */
public class ThemeTemplateProvider implements TemplateProvider {

	@Inject
	private ThemeManager themeManager;
	
	@Inject
	private GlobalSetting globalSetting;
	
	@Override
	public String getName() {
		return "themeTemplate";
	}

	@Override
	public NamedResourceInfo get(String name) {
		String themeName = (String)globalSetting.getValue("theme");
		if (StringUtils.isEmpty(themeName)){
			return null;
		}
		
		ThemePackage themePackage = themeManager.get(ThemeManager.PACKAGE_CATALOG_LOCAL, themeName);
		if (themePackage == null) {
			return null;
		}
		
		return themePackage.getResource("template/" + name);
		
//		ThemeResourceInfo themeResourceInfo = themePackage.getResource(name);
//		if (themeResourceInfo == null || !themeResourceInfo.isTemplate()) {
//			return null;
//		}else{
//			return themeResourceInfo;
//		}
	}

	@Override
	public int getOrder() {
		return PRIORITY_HIGH; // to replace original template.
	}
	
}
