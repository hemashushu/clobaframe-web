package org.archboy.clobaframe.web.template.velocity;

import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import org.apache.velocity.app.VelocityEngine;
import org.archboy.clobaframe.web.template.StaticAttributeLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 * See:
 * {@link org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver} and 
 * {@link org.springframework.web.servlet.view.velocity.VelocityLayoutView}.
 * 
 * @author yang
 */
public class VelocityLayoutViewResolver implements ViewResolver {

	public static final String SETTING_KEY_LAYOUT_URL = "clobaframe.web.template.velocity.layoutUrl";
	public static final String SETTING_KEY_LAYOUT_KEY = "clobaframe.web.template.velocity.layoutKey";
	public static final String SETTING_KEY_SCREEN_CONTENT_KEY = "clobaframe.web.template.velocity.screenContentKey";
	public static final String SETTING_KEY_RESOURCE_NAME_PREFIX = "clobaframe.web.template.velocity.resourceNamePrefix";
	public static final String SETTING_KEY_RESOURCE_NAME_SUFFIX = "clobaframe.web.template.velocity.resourceNameSuffix";
	public static final String SETTING_KEY_CONTENT_TYPE = "clobaframe.web.template.velocity.contentType";
	
	public static final String DEFAULT_LAYOUT_URL = "dayout/default.vm";
	public static final String DEFAULT_LAYOUT_KEY = "layout";
	public static final String DEFAULT_SCREEN_CONTENT_KEY = "screen_content";
	public static final String DEFAULT_RESOURCE_NAME_PREFIX = "";
	public static final String DEFAULT_RESOUTCE_NAME_SUFFIX = ".vm";
	public static final String DEFAULT_CONTENT_TYPE = "text/html; charset=UTF-8";
	
	@Value("${" + SETTING_KEY_LAYOUT_URL + ":" + DEFAULT_LAYOUT_URL + "}")
	private String layoutUrl = DEFAULT_LAYOUT_URL;
	
	@Value("${" + SETTING_KEY_LAYOUT_KEY + ":" + DEFAULT_LAYOUT_KEY + "}")
	private String layoutKey = DEFAULT_LAYOUT_KEY; // the variable key name, in template using "#set ($layout='layout/another.vm')"
	
	@Value("${" + SETTING_KEY_SCREEN_CONTENT_KEY + ":" + DEFAULT_SCREEN_CONTENT_KEY + "}")
	private String screenContentKey = DEFAULT_SCREEN_CONTENT_KEY; // in template using "$screen_content" as placeholder.
	
	@Value("${" + SETTING_KEY_RESOURCE_NAME_PREFIX + ":" + DEFAULT_RESOURCE_NAME_PREFIX + "}")
	private String viewResourcePrefix = DEFAULT_RESOURCE_NAME_PREFIX;
	
	@Value("${" + SETTING_KEY_RESOURCE_NAME_SUFFIX + ":" + DEFAULT_RESOUTCE_NAME_SUFFIX + "}")
	private String viewResourceSuffix = DEFAULT_RESOUTCE_NAME_SUFFIX;
	
	@Value("${" + SETTING_KEY_CONTENT_TYPE + ":" + DEFAULT_CONTENT_TYPE + "}")
	private String contentType = DEFAULT_CONTENT_TYPE;
	
	@Inject
	private VelocityConfigurer velocityEngineFactory;

	@Inject
	private StaticAttributeLoader staticAttributeLoader;
	
	public void setLayoutUrl(String layoutUrl) {
		this.layoutUrl = layoutUrl;
	}

	public void setLayoutKey(String layoutKey) {
		this.layoutKey = layoutKey;
	}

	public void setScreenContentKey(String screenContentKey) {
		this.screenContentKey = screenContentKey;
	}

	public void setViewResourcePrefix(String viewResourcePrefix) {
		this.viewResourcePrefix = viewResourcePrefix;
	}

	public void setViewResourceSuffix(String viewResourceSuffix) {
		this.viewResourceSuffix = viewResourceSuffix;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setStaticAttributeLoader(StaticAttributeLoader staticAttributeLoader) {
		this.staticAttributeLoader = staticAttributeLoader;
	}
	
	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		String resourceName = viewResourcePrefix + viewName + viewResourceSuffix;
		VelocityEngine velocityEngine = velocityEngineFactory.getVelocityEngine();
		Map<String, Object> staticAttributes = staticAttributeLoader.list();
		
		return new VelocityLayoutView(
				velocityEngine,
				resourceName, contentType, staticAttributes, 
				layoutUrl, layoutKey, screenContentKey);
	}
	
}
