package org.archboy.clobaframe.web.theme.inject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.archboy.clobaframe.io.MimeTypeDetector;
import org.archboy.clobaframe.io.NamedResourceInfo;
import org.archboy.clobaframe.io.file.FileBaseResourceInfo;
import org.archboy.clobaframe.io.file.local.DefaultLocalResourceProvider;
import org.archboy.clobaframe.io.file.local.LocalResourceProvider;
import org.archboy.clobaframe.resource.ResourceProvider;
import org.archboy.clobaframe.resource.local.DefaultLocalResourceNameStrategy;
import org.archboy.clobaframe.resource.local.LocalResourceNameStrategy;
import org.archboy.clobaframe.web.theme.local.impl.LocalThemeResourceInfoFactory;
import org.archboy.clobaframe.web.theme.local.impl.LocalThemeResourceInfoFactory;

/**
 *
 * @author yang
 */
public class LocalThemeResourceProvider implements ResourceProvider {

	private LocalResourceProvider localResourceProvider;
	
	public LocalThemeResourceProvider(File basePath, String namePrefix, MimeTypeDetector mimeTypeDetector) {
		
		LocalResourceNameStrategy localWebResourceNameStrategy = new DefaultLocalResourceNameStrategy(basePath, namePrefix);
		LocalThemeResourceInfoFactory localWebResourceInfoFactory = new LocalThemeResourceInfoFactory(mimeTypeDetector, localWebResourceNameStrategy);
			
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
	public NamedResourceInfo getByName(String name) {
		return (NamedResourceInfo)localResourceProvider.getByName(name);
	}

	@Override
	public Collection<NamedResourceInfo> list() {
		List<NamedResourceInfo> resourceInfos = new ArrayList<NamedResourceInfo>();
		for(FileBaseResourceInfo fileBaseResourceInfo : localResourceProvider.list()){
			resourceInfos.add((NamedResourceInfo)fileBaseResourceInfo);
		}
		return resourceInfos;
	}
	
}
