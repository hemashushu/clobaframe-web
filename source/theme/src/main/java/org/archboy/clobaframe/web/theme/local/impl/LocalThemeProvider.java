package org.archboy.clobaframe.web.theme.local.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.io.MimeTypeDetector;
import org.archboy.clobaframe.resource.ResourceProvider;
import org.archboy.clobaframe.resource.ResourceProviderSet;
import org.archboy.clobaframe.web.theme.ThemeManager;
import org.archboy.clobaframe.web.theme.ThemePackage;
import org.archboy.clobaframe.web.theme.ThemeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * @author yang
 */
@Named
public class LocalThemeProvider implements ThemeProvider { //, ResourceLoaderAware, InitializingBean {

	@Inject
	private ResourceLoader resourceLoader;

	@Inject
	private MimeTypeDetector mimeTypeDetector;

	@Inject
	private ResourceProviderSet resourceProviderSet;
	
	// local resource path, usually relative to the 'src/main/webapp' folder.
	// to using this repository, the web application war package must be expended when running.
	public static final String DEFAULT_BASE_RESOURCE_PATH = ""; // "resources/default"
	public static final String DEFAULT_BASE_RESOURCE_NAME_PREFIX = "resources/";
	public static final String DEFAULT_BASE_TEMPLATE_PATH = "";
	public static final String DEFAULT_BASE_TEMPLATE_NAME_PREFIX = "template/";
	public static final String DEFAULT_THEME_RESOURCE_PATH = ""; // "resources/theme";
	public static final String DEFAULT_THEME_RESOURCE_NAME_PREFIX = "";

	public static final String SETTING_KEY_BASE_RESOURCE_PATH = "clobaframe.web.theme.base.resource.path";
	public static final String SETTING_KEY_BASE_RESOURCE_NAME_PREFIX = "clobaframe.web.theme.base.resource.resourceNamePrefix";
	public static final String SETTING_KEY_BASE_TEMPLATE_PATH = "clobaframe.web.theme.base.template.path";
	public static final String SETTING_KEY_BASE_TEMPLATE_NAME_PREFIX = "clobaframe.web.theme.base.template.resourceNamePrefix";
	public static final String SETTING_KEY_THEME_RESOURCE_PATH = "clobaframe.web.theme.local.path";
	public static final String SETTING_KEY_THEME_RESOURCE_NAME_PREFIX = "clobaframe.web.theme.local.resourceNamePrefix";
	
	@Value("${" + SETTING_KEY_BASE_RESOURCE_PATH + ":" + DEFAULT_BASE_RESOURCE_PATH + "}")
	private String baseResourcePath;

	@Value("${" + SETTING_KEY_BASE_RESOURCE_NAME_PREFIX + ":" + DEFAULT_BASE_RESOURCE_NAME_PREFIX + "}")
	private String baseResourceNamePrefix;
	
	@Value("${" + SETTING_KEY_BASE_TEMPLATE_PATH + ":" + DEFAULT_BASE_TEMPLATE_PATH + "}")
	private String baseTemplatePath;
	
	@Value("${" + SETTING_KEY_BASE_TEMPLATE_NAME_PREFIX + ":" + DEFAULT_BASE_TEMPLATE_NAME_PREFIX + "}")
	private String baseTemplateNamePrefix;

	@Value("${" + SETTING_KEY_THEME_RESOURCE_PATH + ":" + DEFAULT_THEME_RESOURCE_PATH + "}")
	private String themeResourcePath;

	@Value("${" + SETTING_KEY_THEME_RESOURCE_NAME_PREFIX + ":" + DEFAULT_THEME_RESOURCE_NAME_PREFIX + "}")
	private String themeResourceNamePrefix;

	private ThemePackage baseThemePackage;

	private File localThemeResourcePath;

	private final Logger logger = LoggerFactory.getLogger(LocalThemeProvider.class);

	//@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setMimeTypeDetector(MimeTypeDetector mimeTypeDetector) {
		this.mimeTypeDetector = mimeTypeDetector;
	}

	public void setResourceProviderSet(ResourceProviderSet resourceProviderSet) {
		this.resourceProviderSet = resourceProviderSet;
	}

	public void setBaseResourcePath(String baseResourcePath) {
		this.baseResourcePath = baseResourcePath;
	}

	public void setBaseResourceNamePrefix(String baseResourceNamePrefix) {
		this.baseResourceNamePrefix = baseResourceNamePrefix;
	}

	public void setBaseTemplatePath(String baseTemplatePath) {
		this.baseTemplatePath = baseTemplatePath;
	}

	public void setBaseTemplateNamePrefix(String baseTemplateNamePrefix) {
		this.baseTemplateNamePrefix = baseTemplateNamePrefix;
	}

	public void setThemeResourcePath(String themeResourcePath) {
		this.themeResourcePath = themeResourcePath;
	}

	public void setThemeResourceNamePrefix(String themeResourceNamePrefix) {
		this.themeResourceNamePrefix = themeResourceNamePrefix;
	}

	@PostConstruct
	//@Override
	public void init() throws Exception {
		// add base local resource
		if (StringUtils.isNotEmpty(baseResourcePath)) {
			File baseResourceBasePath = getFile(baseResourcePath);
			File baseTemplateBasePath = getFile(baseTemplatePath);
			
			List<Map.Entry<File, String>> basePathNames = new ArrayList<Map.Entry<File, String>>();
			if (baseResourceBasePath != null) {
				basePathNames.add(new AbstractMap.SimpleEntry<File, String>(baseResourceBasePath, baseResourceNamePrefix));
			}
			
			if (baseTemplateBasePath != null) {
				basePathNames.add(new AbstractMap.SimpleEntry<File, String>(baseTemplateBasePath, baseTemplateNamePrefix));
			}
			
			baseThemePackage = getThemePackage(ThemeManager.PACKAGE_CATALOG_BASE,
					ThemeManager.PACKAGE_ID_BASE,
					basePathNames);
		}

		// resolve local theme resource path
		if (StringUtils.isNotEmpty(themeResourcePath)) {
			localThemeResourcePath = getFile(themeResourcePath);
			
			if (localThemeResourcePath != null) {
				// insert theme resource provider to web resource manager.
				ResourceProvider resourceProvider = new LocalThemeWebResourceProvider(
					localThemeResourcePath, themeResourceNamePrefix, mimeTypeDetector);
				
				resourceProviderSet.addProvider(resourceProvider);
			}
		}
	}

	private File getFile(String resourcePath) {
		Resource resource = resourceLoader.getResource(resourcePath);

		try {
			File path = resource.getFile();

			if (!path.exists()) {
				logger.error("Can not find the theme resource folder [{}], please ensure "
						+ "unpackage the WAR if you are running web application.", resourcePath);
				return null;
			}
			
			return path;
		} catch (IOException e) {
			logger.error("Load local theme resource repository error, {}", e.getMessage());
		}

		return null;
	}

//	private ThemePackage getThemePackage(String catalog, String name, String resourcePath, String resourceNamePrefix) {
//		File path = getFile(resourcePath);
//		if (path == null) {
//			return null;
//		}
//		return getThemePackage(catalog, name, path, resourceNamePrefix);
//	}
	
	private ThemePackage getThemePackage(String catalog, String id, File path, String resourceNamePrefix) {
		return new LocalThemePackage(catalog, id, path, resourceNamePrefix, mimeTypeDetector);
	}

	private ThemePackage getThemePackage(String catalog, String id, Collection<Map.Entry<File, String>> pathNames) {
		return new MultiPathLocalThemePackage(catalog, id, pathNames, mimeTypeDetector);
	}
	
	@Override
	public Collection<ThemePackage> listPackage() {
		List<ThemePackage> themePackages = new ArrayList<ThemePackage>();
		if (baseThemePackage != null) {
			themePackages.add(baseThemePackage);
		}
		
		if (localThemeResourcePath == null) {
			return themePackages;
		}

		File[] files = localThemeResourcePath.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		
		for(File file : files) {
			String packageResourceNamePrefix = String.format("%s%s/", themeResourceNamePrefix, file.getName());
			
			ThemePackage themePackage = getThemePackage(
					ThemeManager.PACKAGE_CATALOG_LOCAL, file.getName(), 
					file, packageResourceNamePrefix);
			
			if (themePackage != null) {
				themePackages.add(themePackage);
			}
		}
		
		return themePackages;
	}

	@Override
	public ThemePackage get(String catalog, String id) {
		
		if (ThemeManager.PACKAGE_CATALOG_BASE.equals(catalog) &&
				ThemeManager.PACKAGE_ID_BASE.equals(id)) {
			return baseThemePackage;
		}
		
		if (!ThemeManager.PACKAGE_CATALOG_LOCAL.equals(catalog)) {
			return null;
		}
		
		if (localThemeResourcePath == null) {
			return null;
		}
		
		File path = new File(localThemeResourcePath, id);
		if (!path.exists() || path.isFile()) {
			return null;
		}
		
		String packageResourceNamePrefix = String.format("%s%s/", themeResourceNamePrefix, id);
		return getThemePackage(catalog, id, path, packageResourceNamePrefix);
	}

}
