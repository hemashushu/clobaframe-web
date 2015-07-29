package org.archboy.clobaframe.web.theme.inject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.io.MimeTypeDetector;
import org.archboy.clobaframe.io.NamedResourceInfo;
import org.archboy.clobaframe.io.file.FileBaseResourceInfo;
import org.archboy.clobaframe.io.file.local.DefaultLocalResourceProvider;
import org.archboy.clobaframe.io.file.local.LocalResourceProvider;
import org.archboy.clobaframe.resource.ResourceProvider;
import org.archboy.clobaframe.resource.local.DefaultLocalResourceNameStrategy;
import org.archboy.clobaframe.resource.local.LocalResourceNameStrategy;
import static org.archboy.clobaframe.web.theme.local.impl.LocalThemeProvider.DEFAULT_THEME_RESOURCE_NAME_PREFIX;
import static org.archboy.clobaframe.web.theme.local.impl.LocalThemeProvider.DEFAULT_THEME_RESOURCE_PATH;
import static org.archboy.clobaframe.web.theme.local.impl.LocalThemeProvider.SETTING_KEY_THEME_RESOURCE_NAME_PREFIX;
import static org.archboy.clobaframe.web.theme.local.impl.LocalThemeProvider.SETTING_KEY_THEME_RESOURCE_PATH;
import org.archboy.clobaframe.web.theme.local.impl.LocalThemeResourceInfoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * @author yang
 */
public class LocalThemeResourceProvider implements ResourceProvider {

	@Value("${" + SETTING_KEY_THEME_RESOURCE_PATH + ":" + DEFAULT_THEME_RESOURCE_PATH + "}")
	private String themeResourcePath;
		
	@Value("${" + SETTING_KEY_THEME_RESOURCE_NAME_PREFIX + ":" + DEFAULT_THEME_RESOURCE_NAME_PREFIX + "}")
	private String themeResourceNamePrefix;
	
	private File themeResourceDir;
	
	@Inject
	private MimeTypeDetector mimeTypeDetector;
	
	@Inject
	private ResourceLoader resourceLoader;
	
	private LocalResourceProvider localResourceProvider;
	
	private final Logger logger = LoggerFactory.getLogger(LocalThemeResourceProvider.class);
			
	//public LocalThemeResourceProvider(File basePath, String namePrefix, MimeTypeDetector mimeTypeDetector) {
	@PostConstruct
	public void init() throws Exception {	
		if (StringUtils.isEmpty(themeResourcePath)) {
			return;
		}
		
		this.themeResourceDir = getFile(themeResourcePath);
		
		if (themeResourceDir == null) {
			return;
		}
		
		LocalResourceNameStrategy localWebResourceNameStrategy = new DefaultLocalResourceNameStrategy(themeResourceDir, themeResourceNamePrefix);
		LocalThemeResourceInfoFactory localWebResourceInfoFactory = new LocalThemeResourceInfoFactory(mimeTypeDetector, localWebResourceNameStrategy);
			
		this.localResourceProvider = new DefaultLocalResourceProvider(
				themeResourceDir, 
				localWebResourceInfoFactory, 
				localWebResourceNameStrategy);
	}

	@Override
	public String getName() {
		return "localTheme";
	}

	@Override
	public int getOrder() {
		return PRIORITY_NORMAL;
	}

	@Override
	public NamedResourceInfo getByName(String name) {
		if (localResourceProvider == null) {
			return null;
		}else{
			return (NamedResourceInfo)localResourceProvider.getByName(name);
		}
	}

	@Override
	public Collection<NamedResourceInfo> list() {
		List<NamedResourceInfo> resourceInfos = new ArrayList<NamedResourceInfo>();
		
		if (localResourceProvider == null) {
			return resourceInfos;
		}
		
		for(FileBaseResourceInfo fileBaseResourceInfo : localResourceProvider.list()){
			resourceInfos.add((NamedResourceInfo)fileBaseResourceInfo);
		}
		return resourceInfos;
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
}
