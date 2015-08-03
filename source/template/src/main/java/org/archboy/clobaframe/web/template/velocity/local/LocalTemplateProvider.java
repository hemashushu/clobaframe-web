package org.archboy.clobaframe.web.template.velocity.local;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.io.MimeTypeDetector;
import org.archboy.clobaframe.io.NamedResourceInfo;
import org.archboy.clobaframe.io.file.local.DefaultLocalResourceProvider;
import org.archboy.clobaframe.io.file.local.LocalResourceProvider;
import org.archboy.clobaframe.resource.local.DefaultLocalResourceNameStrategy;
import org.archboy.clobaframe.resource.local.LocalResourceInfoFactory;
import org.archboy.clobaframe.resource.local.LocalResourceNameStrategy;
import org.archboy.clobaframe.web.template.TemplateProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * @author yang
 */
@Named
public class LocalTemplateProvider implements TemplateProvider { //, ResourceLoaderAware {

	public static final String SETTING_KEY_LOCAL_PATH = "clobaframe.web.template.local.path";
	public static final String SETTING_KEY_RESOURCE_NAME_PREFIX = "clobaframe.web.template.local.resourceNamePrefix";
	
	public static final String DEFAULT_LOCAL_PATH = "classpath:WEB-INF/view";
	public static final String DEFAULT_RESOURCE_NAME_PREFIX = "";
	
	@Value("${" + SETTING_KEY_LOCAL_PATH + ":" + DEFAULT_LOCAL_PATH + "}")
	private String localPath;
	
	@Value("${" + SETTING_KEY_RESOURCE_NAME_PREFIX + ":" + DEFAULT_RESOURCE_NAME_PREFIX + "}")
	private String resourceNamePrefix;
	
	@Inject
	private ResourceLoader resourceLoader;
	
	@Inject
	private MimeTypeDetector mimeTypeDetector;
	
	private LocalResourceProvider localResourceProvider;
	
	private final Logger logger = LoggerFactory.getLogger(LocalTemplateProvider.class);
	
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public void setResourceNamePrefix(String resourceNamePrefix) {
		this.resourceNamePrefix = resourceNamePrefix;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setMimeTypeDetector(MimeTypeDetector mimeTypeDetector) {
		this.mimeTypeDetector = mimeTypeDetector;
	}
	
	@PostConstruct
	public void init() throws Exception {
		if (StringUtils.isEmpty(localPath)) {
			return;
		}
		
		Resource resource = resourceLoader.getResource(localPath);
		
		//try{
			File basePath = resource.getFile();
			
			if (!basePath.exists()){
				//logger.error("Can not find the view resource folder [{}].", localPath);
				//return;
				throw new FileNotFoundException(
						String.format("Can not find the view resource folder [%s].", localPath));
			}
			
			LocalResourceNameStrategy localResourceNameStrategy = new DefaultLocalResourceNameStrategy(basePath, resourceNamePrefix);
			LocalResourceInfoFactory localResourceInfoFactory = new LocalResourceInfoFactory(mimeTypeDetector, localResourceNameStrategy);
			this.localResourceProvider = new DefaultLocalResourceProvider(basePath, localResourceInfoFactory, localResourceNameStrategy);
			
//		}catch(IOException e){
//			logger.error("Load local view resource provider error, message: {}", e.getMessage());
//		}
	}
	
	@Override
	public String getName() {
		return "localViewResource";
	}

	@Override
	public NamedResourceInfo get(String name) {
//		if (localResourceProvider == null) {
//			return null;
//		}
		
		return (NamedResourceInfo)localResourceProvider.getByName(name);
	}

	@Override
	public int getOrder() {
		return PRIORITY_NORMAL;
	}
	
}
