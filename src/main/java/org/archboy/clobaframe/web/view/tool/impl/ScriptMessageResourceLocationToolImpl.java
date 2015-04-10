package org.archboy.clobaframe.web.view.tool.impl;

import java.io.FileNotFoundException;
import org.archboy.clobaframe.web.view.tool.ScriptMessageResourceLocationTool;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.archboy.clobaframe.webresource.WebResourceInfo;
import org.archboy.clobaframe.webresource.WebResourceManager;

/**
 *
 * @author yang
 */
@Named
public class ScriptMessageResourceLocationToolImpl implements ScriptMessageResourceLocationTool{

	private static final String DEFAULT_I18N_SCRIPT_BASENAME = "js/i18n/messages";

	@Inject
	private WebResourceManager webResourceManager;

	@Value("${web.scriptMessageResource.baseName}")
	private String i18nScriptBaseName = DEFAULT_I18N_SCRIPT_BASENAME;

	private String defaultResourceName;
	private Map<Locale, String> localResourceNames = new HashMap<Locale, String>();

	@PostConstruct
	public void init(){

		Collection<String> resourceNames = webResourceManager.getAllNames();

		String defaultResourceName = i18nScriptBaseName + ".js";
		String regex = "^" + i18nScriptBaseName + "_([a-z]{2})((_)([A-Z]{2}))?\\.js$";
		Pattern pattern = Pattern.compile(regex);

		for(String name : resourceNames){

			if (name.equals(defaultResourceName)){
				defaultResourceName = name;
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
	public String getDefaultMessageName() {
		return defaultResourceName;
	}

	@Override
	public String getDefaultMessageLocation() {
		try {
			return webResourceManager.getLocation(defaultResourceName);
		} catch (FileNotFoundException ex) {
			return null;
		}
	}

	@Override
	public String getLocalMessageName() {
		Locale locale = LocaleContextHolder.getLocale();

		if (locale.equals(Locale.ENGLISH)){
			return null;
		}

		return localResourceNames.get(locale);
	}

	@Override
	public String getLocalMessageLocation() {
		String resourceName = getLocalMessageName();
		if (resourceName == null){
			return null;
		}
		try {
			return webResourceManager.getLocation(resourceName);
		} catch (FileNotFoundException ex) {
			return null;
		}
	}
}
