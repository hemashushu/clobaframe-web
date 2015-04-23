package org.archboy.clobaframe.web.view.tool.impl;

import java.util.ArrayList;
import org.archboy.clobaframe.web.view.tool.ScriptMessageResource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.web.view.tool.PageHeaderProvider;
import org.archboy.clobaframe.web.view.tool.PageHeaderTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.archboy.clobaframe.webresource.WebResourceManager;

/**
 *
 * @author yang
 */
@Named
public class ScriptMessageResourcePageHeaderProvider implements ScriptMessageResource, PageHeaderProvider{

	private static final String DEFAULT_I18N_SCRIPT_PATH = "js/i18n/messages";

	@Inject
	private WebResourceManager webResourceManager;

	@Inject
	private PageHeaderTool pageHeaderTool;
	
	@Value("${clobaframe.web.view.scriptMessageResource.path}")
	private String i18nScriptPath = DEFAULT_I18N_SCRIPT_PATH;

	private String defaultMessageResourceName = "message.js";
	private String localMessageResourceNameRegex = "^message_([a-z]{2})((_)([A-Z]{2}))?\\.js$";
	private Pattern localMessageResourceNamePattern = Pattern.compile(localMessageResourceNameRegex);
		
	private boolean existsDefaultMessageResource;
	private Map<Locale, String> localMessageResourceNames = new HashMap<Locale, String>();

	@PostConstruct
	public void init(){

		// register this instance as page header provider
		pageHeaderTool.addPageHeaderProvider(this);
		
		String basePath = i18nScriptPath + "/";
		
		// find all script message resource.
		Collection<String> resourceNames = webResourceManager.getAllNames();

		for(String resourceName : resourceNames){

			if (!resourceName.startsWith(basePath)) {
				continue;
			}
			
			String filename = resourceName.substring(basePath.length());
			
			// here equals to check whether exists the default message resource.
			if (filename.equals(defaultMessageResourceName)){
				this.existsDefaultMessageResource = true;
				continue;
			}

			Matcher matcher = localMessageResourceNamePattern.matcher(filename);
			if (matcher.find()){
				String langCode = matcher.group(1);
				String countryCode = matcher.group(4);

				Locale locale = null;

				if (countryCode != null){
					locale = new Locale(langCode, countryCode);
				}else{
					locale = new Locale(langCode);
				}

				localMessageResourceNames.put(locale, filename);
			}
		}
	}

	@Override
	public String getDefaultResourceName() {
		return existsDefaultMessageResource ? defaultMessageResourceName : null;
	}

	@Override
	public String getLocalResourceName() {
		Locale locale = LocaleContextHolder.getLocale();

		if (locale.equals(Locale.ENGLISH)){
			return null;
		}

		return localMessageResourceNames.get(locale);
	}

	@Override
	public List<String> getHeaders() {
		List<String> headers = new ArrayList<String>();
		if (existsDefaultMessageResource){
			headers.add(pageHeaderTool.writeResource(defaultMessageResourceName));
		}
		
		String localResourceName = getLocalResourceName();
		if (localResourceName != null) {
			headers.add(pageHeaderTool.writeResource(localResourceName));
		}
		
		return headers;
	}

}
