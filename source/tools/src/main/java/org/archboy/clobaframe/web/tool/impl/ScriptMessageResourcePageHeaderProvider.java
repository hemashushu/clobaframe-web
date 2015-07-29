package org.archboy.clobaframe.web.tool.impl;

import java.util.ArrayList;
import org.archboy.clobaframe.web.tool.ScriptMessageResource;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.resource.ResourceManager;
import org.archboy.clobaframe.web.tool.PageHeaderProvider;
import org.archboy.clobaframe.web.tool.PageHeaderTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author yang
 */
@Named
public class ScriptMessageResourcePageHeaderProvider implements ScriptMessageResource, PageHeaderProvider{

	public static final String DEFAULT_SCRIPT_PATH = ""; //"js/i18n";
	public static final String DEFAULT_SCRIPT_NAME_PREFIX = "messages";
	public static final String DEFAULT_SCRIPT_NAME_SUFFIX = ".js";
	public static final String DEFAULT_LOCALE = "en";
	
	public static final String SETTING_KEY_SCRIPT_PATH = "clobaframe.web.tool.scriptMessage.path";
	public static final String SETTING_KEY_SCRIPT_NAME_PREFIX = "clobaframe.web.tool.scriptMessage.namePrefix";
	public static final String SETTING_KEY_SCRIPT_NAME_SUFFIX = "clobaframe.web.tool.scriptMessage.nameSuffix";
	public static final String SETTING_KEY_DEFAULT_LOCALE = "clobaframe.web.tool.scriptMessage.defaultLocale";
	
	@Inject
	private ResourceManager resourceManager;

	@Inject
	private PageHeaderTool pageHeaderTool;
	
	@Value("${" + SETTING_KEY_SCRIPT_PATH + ":" + DEFAULT_SCRIPT_PATH + "}")
	private String scriptPath;
	
	@Value("${" + SETTING_KEY_SCRIPT_NAME_PREFIX + ":" + DEFAULT_SCRIPT_NAME_PREFIX + "}")
	private String scriptNamePrefix = DEFAULT_SCRIPT_NAME_PREFIX;

	@Value("${" + SETTING_KEY_SCRIPT_NAME_SUFFIX + ":" + DEFAULT_SCRIPT_NAME_SUFFIX + "}")
	private String scriptNameSuffix = DEFAULT_SCRIPT_NAME_SUFFIX;
	
	@Value("${" + SETTING_KEY_DEFAULT_LOCALE + ":" + DEFAULT_LOCALE + "}")
	private Locale defaultLocale = Locale.ENGLISH;
	
	//private String defaultMessageResourceName = "message.js";
	//private String localMessageResourceNameRegex = "^message_([a-z]{2})((_)([A-Z]{2}))?\\.js$";
	//private Pattern localMessageResourceNamePattern = Pattern.compile(localMessageResourceNameRegex);
	
	// cache
	private static final String NOT_FOUND_RESOURCE_NAME = StringUtils.EMPTY;
	
	/**
	 * Cache the resource name.
	 * The names that checked by web resource manager.
	 * 
	 * NULL: not check yet, 
	 * NOT_FOUND_RESOURCE_NAME: no this resource.
	 * Other: the web resource name.
	 */
	private String defaultResourceName;
	
	private Map<Locale, String> localeResourceNames = new HashMap<Locale, String>();

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
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
		return "scriptMessageResourcePageHeader";
	}
	
	@Override
	public String getDefaultResourceName() {
		if (defaultResourceName == null) {
			// check first
			String name = getCompatibleResourceName(null, null);
			defaultResourceName = (name == null) ? NOT_FOUND_RESOURCE_NAME : name;
		}
		
		return defaultResourceName.equals(NOT_FOUND_RESOURCE_NAME) ? 
				null : 
				defaultResourceName;
	}

	/**
	 * 
	 * @param languageCode Language code or NULL or EMPTY.
	 * @param countryCode Country code or NULL or EMPTY.
	 * @return 
	 */
	private String getCompatibleResourceName(String languageCode, String countryCode) {
		String resourceName = buildResourceName(languageCode, countryCode);
		if (resourceManager.get(resourceName) != null){
			return resourceName;
			
		}else if (StringUtils.isNotEmpty(languageCode)){
			// try remove the country code.
			if (StringUtils.isNotEmpty(countryCode)){
				resourceName = buildResourceName(languageCode, null);
				if (resourceManager.get(resourceName) != null) {
					return resourceName;
				}
			}
			
			// try remove the language code.
			resourceName = buildResourceName(null, null);
			if (resourceManager.get(resourceName) != null) {
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
		
		String resourceName = localeResourceNames.get(locale);
		if (resourceName == null) {
			// check first
			String compatibleResourceName = getCompatibleResourceName(locale.getLanguage(), locale.getCountry());
			resourceName = (compatibleResourceName == null) ? NOT_FOUND_RESOURCE_NAME : compatibleResourceName;
			localeResourceNames.put(locale, resourceName);
		}
		
		return resourceName.equals(NOT_FOUND_RESOURCE_NAME) ? null : resourceName;
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
