package org.archboy.clobaframe.web.theme.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.web.theme.ThemeManager;
import org.archboy.clobaframe.web.theme.ThemePackage;
import org.archboy.clobaframe.web.theme.ThemeProvider;
import org.archboy.clobaframe.web.theme.ThemeRepository;
import org.archboy.clobaframe.web.theme.ThemeResourceInfo;
import org.archboy.clobaframe.webresource.WebResourceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 *
 * @author yang
 */
@Named
public class ThemeManagerImpl implements ThemeManager {

	@Inject
	private Collection<ThemeProvider> themeProviders;
	
	@Autowired(required = false)
	private ThemeRepository themeRepository;
	
	@Override
	public Collection<ThemePackage> list(String catalog) {
		List<ThemePackage> themePackages = new ArrayList<ThemePackage>();
		
		for (ThemeProvider themeProvider : themeProviders) {
			for (ThemePackage themePackage : themeProvider.listPackage()){
				if (themePackage.getCatalog().equals(catalog)){
					themePackages.add(themePackage);
				}
			}
		}
		
		return themePackages;
	}

	@Override
	public ThemePackage get(String catalog, String id) {
		for (ThemeProvider themeProvider : themeProviders) {
			ThemePackage themePackage = themeProvider.get(catalog, id);
			if (themePackage != null) {
				return themePackage;
			}
		}
		
		return null;
	}

	@Override
	public ThemePackage create(String catalog, String id) {
		return themeRepository.create(catalog, id);
	}

	@Override
	public ThemePackage update(ThemePackage themePackage, String name, String description, String version, String authorName, String website) {
		return themeRepository.update(themePackage, name, description, version, authorName, website);
	}

	@Override
	public ThemePackage clone(ThemePackage themePackage, boolean includeTemplate, String catalog, String id) {
		Assert.isTrue(!(
				themePackage.getCatalog().equals(catalog) &&
				themePackage.getId().equals(id)));
		
		ThemePackage pkg = themeRepository.create(catalog, id);
		for (ThemeResourceInfo themeResourceInfo : themePackage.listResource()) {
			if (!themeResourceInfo.isTemplate() || includeTemplate)
			themeRepository.save(pkg, themeResourceInfo);
		}
		
		return pkg;
	}

	@Override
	public void save(ThemePackage themePackage, ThemeResourceInfo themeResourceInfo) {
		Assert.isTrue(!themePackage.isReadOnly());
		themeRepository.save(themePackage, themeResourceInfo);
	}

	@Override
	public void delete(ThemePackage themePackage, String id) {
		Assert.isTrue(!themePackage.isReadOnly());
		themeRepository.delete(themePackage, id);
	}

	@Override
	public void delete(ThemePackage themePackage) {
		Assert.isTrue(!themePackage.isReadOnly());
		themeRepository.delete(themePackage);
	}
	
}
