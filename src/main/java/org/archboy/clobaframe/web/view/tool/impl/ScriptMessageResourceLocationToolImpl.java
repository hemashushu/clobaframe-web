package org.archboy.clobaframe.web.view.tool.impl;

import org.archboy.clobaframe.web.view.tool.ScriptMessageResourceLocationTool;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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

	private static final String BASENAME = "js/i18n/messages";

	@Inject
	private WebResourceManager webResourceService;

	@Value("${web.scriptMessageResource.baseName}")
	private String baseName = BASENAME;

	private WebResourceInfo defaultResource;
	private Map<Locale, WebResourceInfo> localResources = new HashMap<Locale, WebResourceInfo>();

	@PostConstruct
	public void init(){

		Collection<WebResourceInfo> resources = webResourceService.getAllResources();

		String defaultResourceName = baseName + ".js";
		String regex = "^" + baseName + "_([a-z]{2})((_)([A-Z]{2}))?\\.js$";
		Pattern pattern = Pattern.compile(regex);

		for(WebResourceInfo resource : resources){
			String name = resource.getName();

			if (name.equals(defaultResourceName)){
				defaultResource = resource;
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

				localResources.put(locale, resource);
			}
		}
	}

	@Override
	public WebResourceInfo getDefaultMessage() {
		return defaultResource;
	}

	@Override
	public String getDefaultMessageLocation() {
		return webResourceService.getLocation(defaultResource);
	}

	@Override
	public WebResourceInfo getLocalMessage() {
		Locale locale = LocaleContextHolder.getLocale();

		if (locale.equals(Locale.ENGLISH)){
			return null;
		}

		return localResources.get(locale);
	}

	@Override
	public String getLocalMessageLocation() {
		WebResourceInfo resource = getLocalMessage();
		if (resource == null){
			return null;
		}
		return webResourceService.getLocation(resource);
	}
}
