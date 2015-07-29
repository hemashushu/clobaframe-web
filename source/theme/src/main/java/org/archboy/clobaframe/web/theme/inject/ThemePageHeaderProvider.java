package org.archboy.clobaframe.web.theme.inject;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.setting.global.GlobalSetting;
import org.archboy.clobaframe.web.tool.PageHeaderProvider;

/**
 * Inject (append) theme resources into page header.
 * 
 * @author yang
 */
public class ThemePageHeaderProvider implements PageHeaderProvider {

	@Inject
	private ThemePageHeaderTool themePageHeaderTool;
	
	@Inject
	private GlobalSetting globalSetting;

	public void setThemePageHeaderTool(ThemePageHeaderTool themePageHeaderTool) {
		this.themePageHeaderTool = themePageHeaderTool;
	}

	public void setGlobalSetting(GlobalSetting globalSetting) {
		this.globalSetting = globalSetting;
	}

	@Override
	public String getName() {
		return "themePageHeader";
	}
	
	@Override
	public List<String> list() {
		String themeName = (String)globalSetting.getValue("theme");
		if (StringUtils.isEmpty(themeName)) {
			return new ArrayList<String>();
		}

		return themePageHeaderTool.list(themeName);
	}
	
}
