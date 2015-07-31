package org.archboy.clobaframe.web.template.velocity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.archboy.clobaframe.io.NamedResourceInfo;
import org.archboy.clobaframe.web.template.TemplateManager;
import org.springframework.ui.velocity.SpringResourceLoader;
import org.springframework.util.Assert;

/**
 * See {@link SpringResourceLoader}.
 * 
 * @author yang
 */
public class DelegateVelocityTemplateResourceLoader extends ResourceLoader {
	
	public static final String NAME = "delegate";
	public static final String RESOURCE_LOADER_CLASS = "delegate.resource.loader.class";
	public static final String RESOURCE_LOADER_CACHE = "delegate.resource.loader.cache";
	public static final String RESOURCE_LOADER_MODIFICATION_CHECK_INTERVAL = "delegate.resource.loader.modificationCheckInterval";
	
	public static final String TEMPLATE_RESOURCE_MANAGER = "delegate.resource.loader.viewResourceManager";

	private TemplateManager templateManager;
	
	@Override
	public void init(ExtendedProperties configuration) {
		this.templateManager = (TemplateManager)this.rsvc.getApplicationAttribute(TEMPLATE_RESOURCE_MANAGER);
	}

	@Override
	public InputStream getResourceStream(String source) throws ResourceNotFoundException {
		NamedResourceInfo namedResourceInfo = getResource(source);

		try {
			return namedResourceInfo.getContent();
		}catch(IOException e){
			throw new VelocityException(e);
		}
	}

	@Override
	public boolean isSourceModified(Resource resource) {
		Date lastModified = getResource(resource.getName()).getLastModified();
		Assert.notNull(lastModified);
		return lastModified.getTime() != resource.getLastModified();
	}

	@Override
	public long getLastModified(Resource resource) {
		Date lastModified = getResource(resource.getName()).getLastModified();
		Assert.notNull(lastModified);
		return lastModified.getTime();
	}

	private NamedResourceInfo getResource(String name) {
		NamedResourceInfo namedResourceInfo = templateManager.get(name);
		
		if (namedResourceInfo == null){
			throw new ResourceNotFoundException(
				"Could not find resource [" + name + "] in the template resource manager.");
		}
		
		return namedResourceInfo;
	}
}
