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
	private ThemePageHeader themePageHeader;
	
	@Inject
	private GlobalSetting globalSetting;

	public void setThemePageHeader(ThemePageHeader themePageHeader) {
		this.themePageHeader = themePageHeader;
	}

	public void setGlobalSetting(GlobalSetting globalSetting) {
		this.globalSetting = globalSetting;
	}
	
	@Override
	public List<String> list() {
		String themeName = (String)globalSetting.get("theme");
		return themePageHeader.list(themeName);
	}
	
}
