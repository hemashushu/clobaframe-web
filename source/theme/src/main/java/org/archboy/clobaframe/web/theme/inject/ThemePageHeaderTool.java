package org.archboy.clobaframe.web.theme.inject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.resource.ResourceManager;
import org.archboy.clobaframe.web.theme.ThemeManager;
import org.archboy.clobaframe.web.theme.ThemePackage;
import org.archboy.clobaframe.web.theme.ThemeResourceInfo;
import org.archboy.clobaframe.web.tool.PageHeaderTool;
import org.springframework.util.Assert;

/**
 *
 * @author yang
 */
@Named
public class ThemePageHeaderTool {
	
	@Inject
	private PageHeaderTool pageHeaderTool;
	
	@Inject
	private ThemeManager themeManager;

	public void setPageHeaderTool(PageHeaderTool pageHeaderTool) {
		this.pageHeaderTool = pageHeaderTool;
	}

	public void setThemeManager(ThemeManager themeManager) {
		this.themeManager = themeManager;
	}
	
	public List<String> list(String themeName) {
		Assert.hasText(themeName);
		
		List<String> headers = new ArrayList<String>();
		ThemePackage themePackage = themeManager.get(ThemeManager.PACKAGE_CATALOG_LOCAL, themeName);
		if (themePackage == null) {
			return headers;
		}
		
		Map<String, Object> attrs = new LinkedHashMap<String, Object>();
		attrs.put("rel", "stylesheet");
		attrs.put("data-source", "theme");
		attrs.put("data-catalog", themePackage.getCatalog());
		attrs.put("data-id", themePackage.getName());
		
		for(ThemeResourceInfo themeResourceInfo : themePackage.listResource()){
			String mimeType = themeResourceInfo.getMimeType();
			String header = pageHeaderTool.writeResource(themeResourceInfo.getName(),
					"link", "href", attrs, 
					ResourceManager.MIME_TYPE_STYLE_SHEET.equals(mimeType));
			headers.add(header);
		}
		
		return headers;
	}
}
