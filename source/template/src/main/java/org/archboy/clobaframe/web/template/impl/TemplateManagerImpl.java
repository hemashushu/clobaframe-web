package org.archboy.clobaframe.web.template.impl;

import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.io.NamedResourceInfo;
import org.archboy.clobaframe.web.template.TemplateManager;
import org.archboy.clobaframe.web.template.TemplateProvider;
import org.springframework.core.OrderComparator;

/**
 *
 * @author yang
 */
@Named
public class TemplateManagerImpl implements TemplateManager {

	@Inject
	private List<TemplateProvider> viewResourceProviders;

	public void setViewResourceProviders(List<TemplateProvider> viewResourceProviders) {
		this.viewResourceProviders = viewResourceProviders;
	}
	
	@PostConstruct
	public void init() throws Exception {
		if (viewResourceProviders != null && !viewResourceProviders.isEmpty()) {
			OrderComparator.sort(viewResourceProviders);
		}
	}
	
	@Override
	public NamedResourceInfo get(String name) {
		for (TemplateProvider provider : viewResourceProviders) {
			NamedResourceInfo namedResourceInfo = provider.get(name);
			if (namedResourceInfo != null) {
				return namedResourceInfo;
			}
		}
		
		return null;
	}
	
}
