package org.archboy.clobaframe.web.theme.local.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.archboy.clobaframe.io.MimeTypeDetector;
import org.archboy.clobaframe.io.file.FileBaseResourceInfo;
import org.archboy.clobaframe.io.file.FileBaseResourceInfoFactory;
import org.archboy.clobaframe.io.file.local.DefaultLocalResourceProvider;
import org.archboy.clobaframe.io.file.local.LocalResourceProvider;
import org.archboy.clobaframe.resource.local.DefaultLocalResourceNameStrategy;
import org.archboy.clobaframe.resource.local.LocalResourceNameStrategy;
import org.archboy.clobaframe.web.theme.ThemePackage;
import org.archboy.clobaframe.web.theme.ThemeResourceInfo;
import org.archboy.clobaframe.web.theme.local.LocalThemeResourceInfo;

/**
 *
 * @author yang
 */
public class LocalThemePackage implements ThemePackage {

	private String catalog;
	private String id;
	private String name;
	
	private static final String infoFileName = "info.json";
	
	private String description;
	private String version;
	private Date lastModified;
	private String authorName;
	private String website;

	private LocalResourceProvider localResourceProvider;
	
	private TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public LocalThemePackage(
			String catalog, String id,
			File basePath, 
			String resourceNamePrefix, 
			MimeTypeDetector mimeTypeDetector) {
		
		
		LocalResourceNameStrategy localWebResourceNameStrategy = new DefaultLocalResourceNameStrategy(
			basePath, resourceNamePrefix);

		LocalThemeResourceInfoFactory fileBaseResourceInfoFactory = new LocalThemeResourceInfoFactory(
			mimeTypeDetector, localWebResourceNameStrategy);

		localResourceProvider = new DefaultLocalResourceProvider(basePath, 
			fileBaseResourceInfoFactory, localWebResourceNameStrategy);
		
		this.catalog = catalog;
		this.id = id;
		
		// load extra info
		try{
			loadPackageInfo(basePath);
		}catch(IOException e){
			// ignore
		}catch(ParseException e){
			//
		}
	}
	
	private void loadPackageInfo(File basePath) throws IOException, ParseException {
		File infoFile = new File(basePath, infoFileName);
		if (!infoFile.exists()) {
			return;
		}
		
		DateFormat dateFormat = new ISO8601DateFormat();
		//objectMapper.setDateFormat(dateFormat);
		
		Map<String, Object> map = objectMapper.readValue(infoFile, typeReference);
		this.description = (String)map.get("description");
		this.version = (String)map.get("version");
		this.lastModified = dateFormat.parse((String)map.get("lastModified"));
		this.authorName = (String)map.get("authorName");
		this.website = (String)map.get("website");

		// set the name
		String nameByInfo = (String)map.get("name");
		if (StringUtils.isNotEmpty(nameByInfo)) {
			this.name = nameByInfo;
		}else{
			this.name = id;
		}
		
		// override
		String catalogByInfo = (String)map.get("catalog");
		if (StringUtils.isNotEmpty(catalogByInfo)) {
			this.catalog = catalogByInfo;
		}
	}
	
	@Override
	public String getCatalog() {
		return catalog;
	}

	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public Date getLastModified() {
		return lastModified;
	}

	@Override
	public String getAuthorName() {
		return authorName;
	}

	@Override
	public String getWebsite() {
		return website;
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Override
	public Collection<ThemeResourceInfo> listResource() {
		Collection<ThemeResourceInfo> themeResourceInfos = new ArrayList<ThemeResourceInfo>();
		for(FileBaseResourceInfo fileBaseResourceInfo : localResourceProvider.list()){
			themeResourceInfos.add((LocalThemeResourceInfo)fileBaseResourceInfo);
		}
		return themeResourceInfos;
	}

	@Override
	public ThemeResourceInfo getResource(String name) {
		return (ThemeResourceInfo)localResourceProvider.getByName(name);
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

		LocalThemePackage other = (LocalThemePackage)o;
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
