package org.archboy.clobaframe.web.template.velocity;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.archboy.clobaframe.io.NamedResourceInfo;
import org.archboy.clobaframe.web.template.TemplateManager;
import org.springframework.ui.velocity.SpringResourceLoader;

/**
 * See {@link SpringResourceLoader}.
 * 
 * @author yang
 */
public class DelegateVelocityTemplateResourceLoader extends ResourceLoader {
	
	public static final String NAME = "delegate";
	public static final String RESOURCE_LOADER_CLASS = "delegate.resource.loader.class";
	public static final String VIEW_RESOURCE_MANAGER = "delegate.resource.loader.viewResourceManager";

	private TemplateManager viewResourceManager;
	
	@Override
	public void init(ExtendedProperties configuration) {
		this.viewResourceManager = (TemplateManager)this.rsvc.getProperty(VIEW_RESOURCE_MANAGER);
	}

	@Override
	public InputStream getResourceStream(String source) throws ResourceNotFoundException {
		NamedResourceInfo namedResourceInfo = viewResourceManager.get(source);
		
		if (namedResourceInfo == null){
			throw new ResourceNotFoundException(
				"Could not find resource [" + source + "] in view resource manager.");
		}

		try {
			return namedResourceInfo.getContent();
		}catch(IOException e){
			throw new VelocityException(e);
		}
	}

	@Override
	public boolean isSourceModified(Resource resource) {
		return false;
	}

	@Override
	public long getLastModified(Resource resource) {
		return 0;
	}
	
}
