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

	private static final String DEFAULT_I18N_SCRIPT_BASENAME = "js/i18n/messages";

	@Inject
	private WebResourceManager webResourceManager;

	@Inject
	private PageHeaderTool pageHeaderTool;
	
	@Value("${clobaframe.web.view.scriptMessageResource.baseName}")
	private String i18nScriptBaseName = DEFAULT_I18N_SCRIPT_BASENAME;

	private String defaultResourceName;
	private Map<Locale, String> localResourceNames = new HashMap<Locale, String>();

	@PostConstruct
	public void init(){

		// register this instance as page header provider
		pageHeaderTool.addPageHeaderProvider(this);
		
		// find all script message resource.
		Collection<String> resourceNames = webResourceManager.getAllNames();

		String defaultName = i18nScriptBaseName + ".js";
		String regex = "^" + i18nScriptBaseName + "_([a-z]{2})((_)([A-Z]{2}))?\\.js$";
		Pattern pattern = Pattern.compile(regex);

		for(String name : resourceNames){

			// here equals to check whether exists the default message resource.
			if (name.equals(defaultName)){
				this.defaultResourceName = name;
				continue;
			}

			Matcher matcher = pattern.matcher(name);
			if (matcher.find()){
				String langCode = matcher.group(1);
				String countryCode = matcher.group(4);

				Locale locale = null;

				if (countryCode != null){
					locale = new Locale(langCode, countryCode);
				}else{
					locale = new Locale(langCode);
				}

				localResourceNames.put(locale, name);
			}
		}
	}

	@Override
	public String getDefaultResourceName() {
		return defaultResourceName;
	}

	@Override
	public String getLocalResourceName() {
		Locale locale = LocaleContextHolder.getLocale();

		if (locale.equals(Locale.ENGLISH)){
			return null;
		}

		return localResourceNames.get(locale);
	}

	@Override
	public List<String> getHeaders() {
		List<String> headers = new ArrayList<String>();
		if (defaultResourceName != null){
			headers.add(pageHeaderTool.writeResource(defaultResourceName));
		}
		
		String localResourceName = getLocalResourceName();
		if (localResourceName != null) {
			headers.add(pageHeaderTool.writeResource(localResourceName));
		}
		
		return headers;
	}

}
