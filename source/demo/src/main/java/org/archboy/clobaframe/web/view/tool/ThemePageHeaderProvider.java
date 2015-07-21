package org.archboy.clobaframe.web.view.tool;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.setting.common.global.GlobalSetting;

/**
 *
 * @author yang
 */
@Named
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
		String themeName = (String)globalSetting.get("theme");
		return themePageHeaderTool.list(themeName);
	}
	
}
