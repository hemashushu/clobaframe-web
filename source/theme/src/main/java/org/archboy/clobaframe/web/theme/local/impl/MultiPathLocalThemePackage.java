package org.archboy.clobaframe.web.theme.local.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.archboy.clobaframe.io.MimeTypeDetector;
import org.archboy.clobaframe.io.file.FileBaseResourceInfo;
import org.archboy.clobaframe.io.file.local.DefaultLocalResourceProvider;
import org.archboy.clobaframe.io.file.local.LocalResourceProvider;
import org.archboy.clobaframe.resource.local.DefaultLocalResourceNameStrategy;
import org.archboy.clobaframe.resource.local.LocalResourceNameStrategy;
import org.archboy.clobaframe.web.theme.ThemePackage;
import org.archboy.clobaframe.web.theme.ThemeResourceInfo;

/**
 * For the base theme resource and template.
 * @author yang
 */
public class MultiPathLocalThemePackage implements ThemePackage {

	private String catalog;
	private String id;
	
	private List<LocalResourceProvider> localResourceProviders = new ArrayList<LocalResourceProvider>();
	
	public MultiPathLocalThemePackage(
			String catalog, String id,
			Collection<Map.Entry<File, String>> pathNames, // base path and name prefix
			MimeTypeDetector mimeTypeDetector) {

		for(Map.Entry<File, String> pathName : pathNames) {
			LocalResourceNameStrategy localWebResourceNameStrategy = new DefaultLocalResourceNameStrategy(
				pathName.getKey(), pathName.getValue());

			LocalThemeResourceInfoFactory fileBaseResourceInfoFactory = new LocalThemeResourceInfoFactory(
				mimeTypeDetector, localWebResourceNameStrategy);

			LocalResourceProvider localResourceProvider = new DefaultLocalResourceProvider(pathName.getKey(), 
				fileBaseResourceInfoFactory, localWebResourceNameStrategy);
			
			localResourceProviders.add(localResourceProvider);
		}
		
		this.catalog = catalog;
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return id;
	}

	@Override
	public String getCatalog() {
		return catalog;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public Date getLastModified() {
		return null;
	}

	@Override
	public String getAuthorName() {
		return null;
	}

	@Override
	public String getWebsite() {
		return null;
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Override
	public Collection<ThemeResourceInfo> listResource() {
		Collection<ThemeResourceInfo> themeResourceInfos = new ArrayList<ThemeResourceInfo>();
		for(LocalResourceProvider localResourceProvider : localResourceProviders) {
			Collection<FileBaseResourceInfo> fileBaseResourceInfos = localResourceProvider.list();
			for(FileBaseResourceInfo fileBaseResourceInfo : fileBaseResourceInfos) {
				themeResourceInfos.add((ThemeResourceInfo)fileBaseResourceInfo);
			}
		}
		
		return themeResourceInfos;
	}

	@Override
	public ThemeResourceInfo getResource(String name) {
		for(LocalResourceProvider localResourceProvider : localResourceProviders) {
			ThemeResourceInfo themeResourceInfo = (ThemeResourceInfo)localResourceProvider.getByName(name);
			if (themeResourceInfo != null) {
				return themeResourceInfo;
			}
		}
		
		return null;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(getCatalog())
				.append(getId())
				.toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null){
			return false;
		}

		if (o == this){
			return true;
		}

		if(o.getClass() != getClass()){
			return false;
		}

		MultiPathLocalThemePackage other = (MultiPathLocalThemePackage)o;
		return new EqualsBuilder()
				.append(getCatalog(), other.getCatalog())
				.append(getId(), other.getId())
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("catalog", getCatalog())
				.append("id", getId())
				.toString();
	}
	
}
