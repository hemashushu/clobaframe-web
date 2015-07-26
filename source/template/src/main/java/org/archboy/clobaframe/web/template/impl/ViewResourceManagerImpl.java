package org.archboy.clobaframe.web.template.impl;

import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.io.NamedResourceInfo;
import org.archboy.clobaframe.web.template.ViewResourceManager;
import org.archboy.clobaframe.web.template.ViewResourceProvider;

/**
 *
 * @author yang
 */
@Named
public class ViewResourceManagerImpl implements ViewResourceManager {

	@Inject
	private Collection<ViewResourceProvider> viewResourceProviders;

	public void setViewResourceProviders(Collection<ViewResourceProvider> viewResourceProviders) {
		this.viewResourceProviders = viewResourceProviders;
	}
	
	@Override
	public NamedResourceInfo get(String name) {
		for (ViewResourceProvider provider : viewResourceProviders) {
			NamedResourceInfo namedResourceInfo = provider.get(name);
			if (namedResourceInfo != null) {
				return namedResourceInfo;
			}
		}
		
		return null;
	}
	
}
