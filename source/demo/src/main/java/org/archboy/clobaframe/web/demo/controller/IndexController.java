package org.archboy.clobaframe.web.demo.controller;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.common.collection.DefaultObjectMap;
import org.archboy.clobaframe.common.collection.ObjectMap;
import org.archboy.clobaframe.setting.global.GlobalSetting;
import org.archboy.clobaframe.web.theme.ThemeManager;
import org.archboy.clobaframe.web.theme.ThemePackage;
import org.archboy.clobaframe.web.demo.tool.ThemePageHeaderTool;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author yang
 */
@Controller
public class IndexController {

	@Inject
	private GlobalSetting globalSetting;
	
	@Inject
	private ThemeManager themeManager;
	
	@Inject
	private ThemePageHeaderTool themePageHeaderTool;

	public void setGlobalSetting(GlobalSetting globalSetting) {
		this.globalSetting = globalSetting;
	}

	public void setThemeManager(ThemeManager themeManager) {
		this.themeManager = themeManager;
	}

	public void setThemePageHeaderTool(ThemePageHeaderTool themePageHeaderTool) {
		this.themePageHeaderTool = themePageHeaderTool;
	}
	
	@RequestMapping("/")
	public String index(Locale locale, Model model){
		
		ObjectMap pageOptions = new DefaultObjectMap()
				.add("locale", locale);
		
		model.addAttribute("pageOptions", pageOptions);
		
		return "index";
	}
	
	@ResponseBody
	@RequestMapping("/setlanguage")
	public ObjectMap setLanguage(Locale locale) {
		// route for org.springframework.web.servlet.i18n.LocaleChangeInterceptor
		return new DefaultObjectMap()
				.add("result", "success")
				.add("locale", locale.toLanguageTag());
	}
	
	@ResponseBody
	@RequestMapping("/settheme")
	public List<String> setTheme(@RequestParam(value = "name", required = false, defaultValue = "") String name) throws FileNotFoundException {
		if (StringUtils.isNotEmpty(name)) {
			ThemePackage themePackage = themeManager.get(ThemeManager.PACKAGE_CATALOG_LOCAL, name);
			if (themePackage == null) {
				throw new FileNotFoundException("no this theme:" + name);
			}
		}
		
		globalSetting.set("theme", name);
		
		return themePageHeaderTool.list(name);
	}
	
	@RequestMapping("/error")
	public String error(@RequestParam(value = "code", required = false, defaultValue = "") String code,
			Model model){
		model.addAttribute("code", code);
		return "error";
	}
}
