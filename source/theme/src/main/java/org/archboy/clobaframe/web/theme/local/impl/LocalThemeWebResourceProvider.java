package org.archboy.clobaframe.web.theme.local.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.archboy.clobaframe.io.MimeTypeDetector;
import org.archboy.clobaframe.io.file.FileBaseResourceInfo;
import org.archboy.clobaframe.io.file.FileBaseResourceInfoFactory;
import org.archboy.clobaframe.io.file.local.DefaultLocalResourceProvider;
import org.archboy.clobaframe.io.file.local.LocalFileNameStrategy;
import org.archboy.clobaframe.io.file.local.LocalResourceProvider;
import org.archboy.clobaframe.web.theme.ThemeResourceInfo;
import org.archboy.clobaframe.web.theme.local.LocalThemeResourceInfo;
import org.archboy.clobaframe.webresource.WebResourceInfo;
import org.archboy.clobaframe.webresource.WebResourceProvider;
import org.archboy.clobaframe.webresource.local.DefaultLocalWebResourceNameStrategy;
import org.archboy.clobaframe.webresource.local.LocalWebResourceInfoFactory;
import org.archboy.clobaframe.webresource.local.LocalWebResourceNameStrategy;

/**
 *
 * @author yang
 */
public class LocalThemeWebResourceProvider implements WebResourceProvider {

	private LocalResourceProvider localResourceProvider;
	
	public LocalThemeWebResourceProvider(File basePath, String namePrefix, MimeTypeDetector mimeTypeDetector) {
		
		LocalWebResourceNameStrategy localWebResourceNameStrategy = new DefaultLocalWebResourceNameStrategy(basePath, namePrefix);
		LocalWebResourceInfoFactory localWebResourceInfoFactory = new LocalWebResourceInfoFactory(mimeTypeDetector, localWebResourceNameStrategy);
			
		this.localResourceProvider = new DefaultLocalResourceProvider(
				basePath, 
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
	public WebResourceInfo getByName(String name) {
		return (WebResourceInfo)localResourceProvider.getByName(name);
	}

	@Override
	public Collection<WebResourceInfo> list() {
		List<WebResourceInfo> webResourceInfos = new ArrayList<WebResourceInfo>();
		for(FileBaseResourceInfo fileBaseResourceInfo : localResourceProvider.list()){
			webResourceInfos.add((WebResourceInfo)fileBaseResourceInfo);
		}
		return webResourceInfos;
	}
	
}
