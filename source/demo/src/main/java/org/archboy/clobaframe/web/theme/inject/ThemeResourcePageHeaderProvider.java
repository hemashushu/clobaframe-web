package org.archboy.clobaframe.web.theme.inject;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.setting.global.GlobalSetting;
import org.archboy.clobaframe.web.tool.PageHeaderProvider;

/**
 * Inject (append) theme resources into page header.
 * 
 * @author yang
 */
public class ThemeResourcePageHeaderProvider implements PageHeaderProvider {

	@Inject
	private ThemeResourcePageHeaderTool themeResourcePageHeaderTool;
	
	@Inject
	private GlobalSetting globalSetting;

	public void setThemeResourcePageHeaderTool(ThemeResourcePageHeaderTool themeResourcePageHeaderTool) {
		this.themeResourcePageHeaderTool = themeResourcePageHeaderTool;
	}

	public void setGlobalSetting(GlobalSetting globalSetting) {
		this.globalSetting = globalSetting;
	}

	@Override
	public int getOrder() {
		return PRIORITY_NORMAL;
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

		return themeResourcePageHeaderTool.listFixedResourceHeaders(themeName);
	}
	
}
