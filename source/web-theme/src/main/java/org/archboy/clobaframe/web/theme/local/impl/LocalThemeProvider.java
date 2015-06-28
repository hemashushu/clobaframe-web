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
import org.archboy.clobaframe.web.theme.ThemeManager;
import org.archboy.clobaframe.web.theme.ThemePackage;
import org.archboy.clobaframe.web.theme.ThemeProvider;
import org.archboy.clobaframe.webresource.WebResourceProvider;
import org.archboy.clobaframe.webresource.WebResourceProviderSet;
import org.archboy.clobaframe.webresource.local.DefaultLocalWebResourceNameStrategy;
import org.archboy.clobaframe.webresource.local.LocalWebResourceNameStrategy;
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
public class LocalThemeProvider implements ThemeProvider {

	@Inject
	private ResourceLoader resourceLoader;

	@Inject
	private MimeTypeDetector mimeTypeDetector;

	@Inject
	private WebResourceProviderSet webResourceProviderSet;
	
	// local resource path, usually relative to the 'src/main/webapp' folder.
	// to using this repository, the web application war package must be expended when running.
	private static final String DEFAULT_BASE_RESOURCE_PATH = ""; // "resources/default"
	private static final String DEFAULT_BASE_RESOURCE_NAME_PREFIX = "resources/";

	@Value("${clobaframe.web.theme.base.resource.path:" + DEFAULT_BASE_RESOURCE_PATH + "}")
	private String baseResourcePath;

	@Value("${clobaframe.web.theme.base.resource.resourceNamePrefix:" + DEFAULT_BASE_RESOURCE_NAME_PREFIX + "}")
	private String baseResourceNamePrefix;

	private static final String DEFAULT_BASE_TEMPLATE_PATH = "";
	private static final String DEFAULT_BASE_TEMPLATE_NAME_PREFIX = "template/";
	
	@Value("${clobaframe.web.theme.base.template.path:" + DEFAULT_BASE_TEMPLATE_PATH + "}")
	private String baseTemplatePath;
	
	@Value("${clobaframe.web.theme.base.template.resourceNamePrefix:" + DEFAULT_BASE_TEMPLATE_NAME_PREFIX + "}")
	private String baseTemplateNamePrefix;
	
	private static final String DEFAULT_THEME_RESOURCE_PATH = ""; // "resources/theme";
	private static final String DEFAULT_THEME_RESOURCE_NAME_PREFIX = "";

	@Value("${clobaframe.web.theme.local.path:" + DEFAULT_THEME_RESOURCE_PATH + "}")
	private String themeResourcePath;

	@Value("${clobaframe.web.theme.local.resourceNamePrefix:" + DEFAULT_THEME_RESOURCE_NAME_PREFIX + "}")
	private String themeResourceNamePrefix;

	private ThemePackage baseThemePackage;

	private File localThemeResourcePath;

	private final Logger logger = LoggerFactory.getLogger(LocalThemeProvider.class);

	@PostConstruct
	public void init() {
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
					ThemeManager.PACKAGE_NAME_BASE,
					basePathNames);
		}

		// resolve local theme resource path
		if (StringUtils.isNotEmpty(themeResourcePath)) {
			localThemeResourcePath = getFile(themeResourcePath);
			
			if (localThemeResourcePath != null) {
				// insert theme resource provider to web resource manager.
				WebResourceProvider webResourceProvider = new LocalThemeWebResourceProvider(
					localThemeResourcePath, themeResourceNamePrefix, mimeTypeDetector);
				
				webResourceProviderSet.addProvider(webResourceProvider);
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
	
	private ThemePackage getThemePackage(String catalog, String name, File path, String resourceNamePrefix) {
		return new LocalThemePackage(catalog, name, path, resourceNamePrefix, mimeTypeDetector);
	}

	private ThemePackage getThemePackage(String catalog, String name, Collection<Map.Entry<File, String>> pathNames) {
		return new MultiPathLocalThemePackage(catalog, name, pathNames, mimeTypeDetector);
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
	public ThemePackage get(String catalog, String name) {
		
		if (ThemeManager.PACKAGE_CATALOG_BASE.equals(catalog) &&
				ThemeManager.PACKAGE_NAME_BASE.equals(name)) {
			return baseThemePackage;
		}
		
		if (!ThemeManager.PACKAGE_CATALOG_LOCAL.equals(catalog)) {
			return null;
		}
		
		if (localThemeResourcePath == null) {
			return null;
		}
		
		File path = new File(localThemeResourcePath, name);
		if (!path.exists() || path.isFile()) {
			return null;
		}
		
		String packageResourceNamePrefix = String.format("%s%s/", themeResourceNamePrefix, name);
		return getThemePackage(catalog, name, path, packageResourceNamePrefix);
	}

}
