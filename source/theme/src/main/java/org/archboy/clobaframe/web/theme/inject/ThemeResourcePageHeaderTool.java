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
public class ThemeResourcePageHeaderTool {
	
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
		
		Map<String, Object> attributes = new LinkedHashMap<String, Object>();
		attributes.put("data-source", "theme");
		attributes.put("data-catalog", themePackage.getCatalog());
		attributes.put("data-id", themePackage.getName());
		
		for(ThemeResourceInfo themeResourceInfo : themePackage.listResource()){
			// filter resource mime type
			String mimeType = themeResourceInfo.getMimeType();
			if (!ResourceManager.MIME_TYPE_STYLE_SHEET.equals(mimeType) &&
					!ResourceManager.MIME_TYPE_JAVA_SCRIPT.contains(mimeType)){
				continue;
			}
			
			// write header
			String resourceName = "theme/" + themeName + "/" + themeResourceInfo.getName();
			String header = pageHeaderTool.writeResource(resourceName, attributes);
			headers.add(header);
		}
		
		return headers;
	}
}
