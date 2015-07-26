package org.archboy.clobaframe.web.template.impl;

import java.util.HashMap;
import java.util.Map;
import org.archboy.clobaframe.web.template.StaticAttributeLoader;

/**
 * Define the attribute objects by the IoC manager.
 * 
 * @author yang
 */
public class SimpleStaticAttributeLoader implements StaticAttributeLoader {
	
	private Map<String, Object> staticAttributes = new HashMap<>();
	
	public void setAttributesMap(Map<String, ?> attributes) {
		if (attributes != null) {
			this.staticAttributes.putAll(attributes);
		}
	}

	@Override
	public Map<String, Object> list() {
		return staticAttributes;
	}
}
