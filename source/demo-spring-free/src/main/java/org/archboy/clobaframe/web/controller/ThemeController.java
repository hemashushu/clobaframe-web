package org.archboy.clobaframe.web.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.setting.global.GlobalSetting;
import org.archboy.clobaframe.web.theme.ThemeManager;
import org.archboy.clobaframe.web.theme.ThemePackage;
import org.archboy.clobaframe.web.theme.inject.ThemeResourcePageHeaderTool;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author yang
 */
@Controller
public class ThemeController implements Ordered {

	@Inject
	private GlobalSetting globalSetting;
	
	@Inject
	private ThemeManager themeManager;
	
	@Inject
	private ThemeResourcePageHeaderTool themeResourcePageHeaderTool;

	public void setGlobalSetting(GlobalSetting globalSetting) {
		this.globalSetting = globalSetting;
	}

	public void setThemeManager(ThemeManager themeManager) {
		this.themeManager = themeManager;
	}

	public void setThemeResourcePageHeaderTool(ThemeResourcePageHeaderTool themeResourcePageHeaderTool) {
		this.themeResourcePageHeaderTool = themeResourcePageHeaderTool;
	}

	@Override
	public int getOrder() {
		return 50;
	}
	
	@ResponseBody
	@RequestMapping("^/settheme$")
	public Collection<String> setTheme(
			@RequestParam(value = "name", required = false, defaultValue = "") String themeName) 
			throws FileNotFoundException {
		
		Collection<String> headers = new ArrayList<>();
		
		if (StringUtils.isNotEmpty(themeName)) {
			ThemePackage themePackage = themeManager.get(ThemeManager.PACKAGE_CATALOG_LOCAL, themeName);
			if (themePackage == null) {
				throw new FileNotFoundException("No this theme: " + themeName);
			}
			globalSetting.set("theme", themeName);
			headers = themeResourcePageHeaderTool.listFixedResourceHeaders(themeName);
		}else{
			globalSetting.set("theme", StringUtils.EMPTY);
		}
		
		return headers;
	}
	
}
