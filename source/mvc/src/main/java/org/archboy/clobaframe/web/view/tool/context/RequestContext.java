package org.archboy.clobaframe.web.view.tool.context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author yang
 */
public class RequestContext {
	
	private Locale locale;
	private Map<String, Object> attributes = new HashMap<String, Object>();

	public Locale getLocale() {
		return locale;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	public void addAttribute(String name, Object value) {
		attributes.put(name, value);
	}
	
}
