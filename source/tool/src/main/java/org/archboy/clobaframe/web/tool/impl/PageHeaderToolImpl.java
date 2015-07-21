package org.archboy.clobaframe.web.tool.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.io.NamedResourceInfo;
import org.archboy.clobaframe.resource.ResourceManager;
import org.archboy.clobaframe.web.tool.PageHeaderTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yang
 */
@Named
public class PageHeaderToolImpl implements PageHeaderTool {

	/**
	 * About the script and style sheet header template.
	 * Specifying "type" attributes in these contexts is not necessary as
	 * HTML5 implies text/css and text/javascript as defaults.
	 * i.e.
	 * type="text/javascript" and type="text/css" is not necessary in HTML5.
	 */

	private static final String SCRIPT_TEMPLATE = "<script src=\"%s\"></script>";
	private static final String STYLESHEET_TEMPLATE = "<link href=\"%s\" rel=\"stylesheet\">";
	
	@Inject
	private ResourceManager resourceManager;

	private final Logger logger = LoggerFactory.getLogger(PageHeaderToolImpl.class);

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	@Override
	public String write(String tagName, Map<String, Object> attributes, boolean closeTag) {
		StringBuilder builder = new StringBuilder();
		builder.append("<");
		builder.append(tagName);
		
		for(Map.Entry<String, Object> entry : attributes.entrySet()){
			builder.append(" ");
			builder.append(entry.getKey());
			builder.append("=\"");
			builder.append(entry.getValue());
			builder.append("\"");
		}
		
		builder.append(">");
		
		if (closeTag) {
			builder.append("</");
			builder.append(tagName);
			builder.append(">");
		}
		return builder.toString();
	}
	
	@Override
	public String writeResource(String name) {

		NamedResourceInfo resource = resourceManager.getServedResource(name);
		if (resource == null) {
			return StringUtils.EMPTY;
		}
		
		String mimeType = resource.getMimeType();
		if (ResourceManager.MIME_TYPE_JAVA_SCRIPT.contains(mimeType)){
			return String.format(SCRIPT_TEMPLATE, resourceManager.getLocation(resource));
		}else if (ResourceManager.MIME_TYPE_STYLE_SHEET.equals(mimeType)){
			return String.format(STYLESHEET_TEMPLATE, resourceManager.getLocation(resource));
		}else{
			// unsupport resource type
			logger.error("Unsupport mime type, resource: " + name);
			return StringUtils.EMPTY;
		}
	}

	@Override
	public String writeResources(Collection<String> names){
		return writeResources(names, null);
	}
	
	@Override
	public String writeResources(Collection<String> names, String separator) {
		if (names == null || names.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		List<String> results = new ArrayList<String>();

		for(String name : names){
			String result = writeResource(name);
			if (result != null){
				results.add(result);
			}
		}

		if (results.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		return StringUtils.join(results, separator);
	}

	@Override
	public String writeResource(String resourceName, 
			String tagName, 
			String locationAttributeName, 
			Map<String, Object> otherAttributes,
			boolean closeTag) {
		
		NamedResourceInfo resource = resourceManager.getServedResource(resourceName);

		if (resource == null) {
			return StringUtils.EMPTY;
		}
		
		String location = resourceManager.getLocation(resource);

		if (otherAttributes == null){
			otherAttributes = new LinkedHashMap<String, Object>();
		}

		otherAttributes.put(locationAttributeName, location);
		return write(tagName, otherAttributes, closeTag);
	}
	
}
