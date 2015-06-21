package org.archboy.clobaframe.web.theme.local.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.archboy.clobaframe.io.file.FileBaseResourceInfo;
import org.archboy.clobaframe.io.file.FileBaseResourceInfoFactory;
import org.archboy.clobaframe.io.file.local.DefaultLocalResourceProvider;
import org.archboy.clobaframe.io.file.local.LocalFileNameStrategy;
import org.archboy.clobaframe.web.theme.ThemePackage;
import org.archboy.clobaframe.web.theme.ThemeResourceInfo;
import org.archboy.clobaframe.web.theme.local.LocalThemeResourceInfo;

/**
 *
 * @author yang
 */
public class LocalThemePackage extends DefaultLocalResourceProvider implements ThemePackage {

	private String catalog;
	private String name;
	
	private static final String infoFileName = "info.json";
	
	private String description;
	private String version;
	private Date lastModified;
	private String authorName;
	private String website;

	private TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public LocalThemePackage(
			String catalog, String name,
			File basePath, 
			FileBaseResourceInfoFactory fileBaseResourceInfoFactory, 
			LocalFileNameStrategy localFileNameStrategy) {
		super(basePath, fileBaseResourceInfoFactory, localFileNameStrategy);
		
		this.catalog = catalog;
		this.name = name;
		
		try{
			loadPackageInfo(basePath);
		}catch(IOException e){
			// ignore
		}
	}
	
	private void loadPackageInfo(File basePath) throws IOException {
		File infoFile = new File(basePath, infoFileName);
		if (!infoFile.exists()) {
			return;
		}
		
		Map<String, Object> map = objectMapper.readValue(infoFile, typeReference);
		this.description = (String)map.get("description");
		this.version = (String)map.get("version");
		this.lastModified = (Date)map.get("lastModified");
		this.authorName = (String)map.get("authorName");
		this.website = (String)map.get("website");
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getCatalog() {
		return catalog;
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
	public Collection<ThemeResourceInfo> getResources() {
		Collection<ThemeResourceInfo> themeResourceInfos = new ArrayList<ThemeResourceInfo>();
		for(FileBaseResourceInfo fileBaseResourceInfo : super.getAll()){
			themeResourceInfos.add((LocalThemeResourceInfo)fileBaseResourceInfo);
		}
		return themeResourceInfos;
	}

	@Override
	public ThemeResourceInfo getResource(String name) {
		return (ThemeResourceInfo)super.getByName(name);
	}
	
}
