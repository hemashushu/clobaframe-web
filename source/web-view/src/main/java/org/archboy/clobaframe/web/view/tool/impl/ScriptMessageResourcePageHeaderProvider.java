package org.archboy.clobaframe.web.view.tool.impl;

import java.util.ArrayList;
import org.archboy.clobaframe.web.view.tool.ScriptMessageResource;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.web.view.tool.PageHeaderExtensionTool;
import org.archboy.clobaframe.web.view.tool.PageHeaderProvider;
import org.archboy.clobaframe.web.view.tool.PageHeaderTool;
import org.archboy.clobaframe.webresource.WebResourceInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.archboy.clobaframe.webresource.WebResourceManager;

/**
 *
 * @author yang
 */
@Named
public class ScriptMessageResourcePageHeaderProvider implements ScriptMessageResource, PageHeaderProvider{

	private static final String DEFAULT_I18N_SCRIPT_PATH = ""; //"js/i18n";
	private static final String DEFAULT_I18N_SCRIPT_NAME_PREFIX = "messages";
	private static final String DEFAULT_I18N_SCRIPT_NAME_SUFFIX = ".js";
	private static final String DEFAULT_LOCALE = "en";
	
	@Inject
	private WebResourceManager webResourceManager;

	@Inject
	private PageHeaderTool pageHeaderTool;
	
	@Value("${clobaframe.web.view.scriptMessageResource.path:" + DEFAULT_I18N_SCRIPT_PATH + "}")
	private String scriptPath;
	
	@Value("${clobaframe.web.view.scriptMessageResource.namePrefix:" + DEFAULT_I18N_SCRIPT_NAME_PREFIX + "}")
	private String scriptNamePrefix;

	@Value("${clobaframe.web.view.scriptMessageResource.nameSuffix:" + DEFAULT_I18N_SCRIPT_NAME_SUFFIX + "}")
	private String scriptNameSuffix;
	
	@Value("${clobaframe.web.view.scriptMessageResource.defaultLocale:" + DEFAULT_LOCALE + "}")
	private Locale defaultLocale;
	
	//private String defaultMessageResourceName = "message.js";
	//private String localMessageResourceNameRegex = "^message_([a-z]{2})((_)([A-Z]{2}))?\\.js$";
	//private Pattern localMessageResourceNamePattern = Pattern.compile(localMessageResourceNameRegex);
	
	// cache
	private static final String RESOURCE_NOT_FOUND_NAME = StringUtils.EMPTY;
	
	/**
	 * Cache the resource name that checked by web resource manager.
	 * 
	 * NULL: not check yet, 
	 * RESOURCE_NOT_FOUND_NAME: no this resource.
	 * Other: the web resource name.
	 */
	private String nameOfDefaultResource;
	private Map<Locale, String> localResourceCacheNames = new HashMap<Locale, String>();

	public void setWebResourceManager(WebResourceManager webResourceManager) {
		this.webResourceManager = webResourceManager;
	}

	public void setPageHeaderTool(PageHeaderTool pageHeaderTool) {
		this.pageHeaderTool = pageHeaderTool;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	public void setScriptNamePrefix(String scriptNamePrefix) {
		this.scriptNamePrefix = scriptNamePrefix;
	}

	public void setScriptNameSuffix(String scriptNameSuffix) {
		this.scriptNameSuffix = scriptNameSuffix;
	}

	public void setDefaultLocale(Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}
	
	public void register(PageHeaderExtensionTool pageHeaderExtensionTool){
		pageHeaderExtensionTool.addProvider(this);
	}
	
//	@PostConstruct
//	public void init(){
//
//		// register this instance as page header provider
//		pageHeaderTool.addPageHeaderProvider(this);
//		
//		String basePath = i18nScriptPath + "/";
//		
//		// find all script message resource.
//		Collection<String> resourceNames = webResourceManager.getAllNames();
//
//		for(String resourceName : resourceNames){
//
//			if (!resourceName.startsWith(basePath)) {
//				continue;
//			}
//			
//			String filename = resourceName.substring(basePath.length());
//			
//			// here equals to check whether exists the default message resource.
//			if (filename.equals(defaultMessageResourceName)){
//				this.existsDefaultMessageResource = true;
//				continue;
//			}
//
//			Matcher matcher = localMessageResourceNamePattern.matcher(filename);
//			if (matcher.find()){
//				String langCode = matcher.group(1);
//				String countryCode = matcher.group(4);
//
//				Locale locale = null;
//
//				if (countryCode != null){
//					locale = new Locale(langCode, countryCode);
//				}else{
//					locale = new Locale(langCode);
//				}
//
//				localMessageResourceNames.put(locale, filename);
//			}
//		}
//	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}
	
	@Override
	public String getDefaultResourceName() {
		if (nameOfDefaultResource == null) {
			// check first
			String name = getCompatibleResourceName(null, null);
			nameOfDefaultResource = (name == null) ? RESOURCE_NOT_FOUND_NAME : name;
		}
		
		return nameOfDefaultResource.equals(RESOURCE_NOT_FOUND_NAME) ? 
				null : 
				nameOfDefaultResource;
	}

	/**
	 * 
	 * @param languageCode Language code or NULL or EMPTY.
	 * @param countryCode Country code or NULL or EMPTY.
	 * @return 
	 */
	private String getCompatibleResourceName(String languageCode, String countryCode) {
		String resourceName = buildResourceName(languageCode, countryCode);
		if (webResourceManager.getServerResource(resourceName) != null){
			return resourceName;
			
		}else if (StringUtils.isNotEmpty(languageCode)){
			
			// try remove the country code.
			if (StringUtils.isNotEmpty(countryCode)){
				resourceName = buildResourceName(languageCode, null);
				if (webResourceManager.getServerResource(resourceName) != null) {
					return resourceName;
				}
			}
			
			// try remove the language code.
			resourceName = buildResourceName(null, null);
			if (webResourceManager.getServerResource(resourceName) != null) {
				return resourceName;
			}
		}
		
		return null;
	}
	
	/**
	 * Return scriptPath + scriptNamePrefix + ["_" + language code + ] + ["_" + country code + ] + scriptNameSuffix.
	 * 
	 * @param languageCode
	 * @param countryCode
	 * @return 
	 */
	private String buildResourceName(String languageCode, String countryCode) {
		StringBuilder builder = new StringBuilder();
		builder.append(scriptPath);
		builder.append("/");
		builder.append(scriptNamePrefix);
		if (StringUtils.isNotEmpty(languageCode)){
			builder.append("_");
			builder.append(languageCode);
		}
		
		if (StringUtils.isNotEmpty(countryCode)) {
			builder.append("_");
			builder.append(countryCode);
		}
		
		builder.append(scriptNameSuffix);
		return builder.toString();
	}
	
	@Override
	public String getLocalResourceName() {
		Locale locale = LocaleContextHolder.getLocale();

		if (locale.equals(defaultLocale)){
			return getDefaultResourceName();
		}
		
		String cacheName = localResourceCacheNames.get(locale);
		if (cacheName == null) {
			// check first
			String compatibleResourceName = getCompatibleResourceName(locale.getLanguage(), locale.getCountry());
			cacheName = (compatibleResourceName == null) ? RESOURCE_NOT_FOUND_NAME : compatibleResourceName;
			localResourceCacheNames.put(locale, cacheName);
		}
		
		return cacheName.equals(RESOURCE_NOT_FOUND_NAME) ? null : cacheName;
	}

	@Override
	public List<String> list() {
		List<String> headers = new ArrayList<String>();
		
		if (StringUtils.isEmpty(scriptPath)){
			// no script message resource config.
			return headers;
		}
		
		String defaultName = getDefaultResourceName();
		if (defaultName != null){
			headers.add(pageHeaderTool.writeResource(defaultName));
		}
		
		String localName = getLocalResourceName();
		if (localName != null && !localName.equals(defaultName)) {
			headers.add(pageHeaderTool.writeResource(localName));
		}
		
		return headers;
	}

}
