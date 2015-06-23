package org.archboy.clobaframe.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.setting.global.GlobalSetting;
import org.archboy.clobaframe.web.theme.ThemeManager;
import org.archboy.clobaframe.web.theme.ThemePackage;
import org.archboy.clobaframe.web.theme.ThemeResourceInfo;
import org.archboy.clobaframe.web.view.tool.PageHeaderProvider;
import org.archboy.clobaframe.web.view.tool.PageHeaderTool;
import org.archboy.clobaframe.webresource.WebResourceManager;

/**
 *
 * @author yang
 */
@Named
public class ThemePageHeaderProvider implements PageHeaderProvider {

	@Inject
	private PageHeaderTool pageHeaderTool;
	
	@Inject
	private ThemeManager themeManager;
	
	@Inject
	private GlobalSetting globalSetting;
	
	@Override
	public List<String> getHeaders() {
		List<String> headers = new ArrayList<String>();
		
		String themeName = (String)globalSetting.get("theme");
		if (StringUtils.isEmpty(themeName)) {
			return headers;
		}
		
		ThemePackage themePackage = themeManager.get(ThemeManager.PACKAGE_CATALOG_LOCAL, themeName);
		if (themePackage == null) {
			return headers;
		}
		
		for(ThemeResourceInfo themeResourceInfo : themePackage.getResources()){
			String mimeType = themeResourceInfo.getMimeType();
			if (WebResourceManager.MIME_TYPE_STYLE_SHEET.equals(mimeType)) {
				String header = pageHeaderTool.writeResource(themeResourceInfo.getName());
				headers.add(header);
			}
		}
		
		return headers;
	}
	
}
