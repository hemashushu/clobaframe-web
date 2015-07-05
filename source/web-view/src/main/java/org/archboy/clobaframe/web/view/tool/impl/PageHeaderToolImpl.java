package org.archboy.clobaframe.web.view.tool.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.web.view.tool.PageHeaderProvider;
import org.archboy.clobaframe.web.view.tool.PageHeaderTool;
import org.archboy.clobaframe.webresource.WebResourceInfo;
import org.archboy.clobaframe.webresource.WebResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

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
	private WebResourceManager webResourceManager;

	private final Logger logger = LoggerFactory.getLogger(PageHeaderToolImpl.class);

	public void setWebResourceManager(WebResourceManager webResourceManager) {
		this.webResourceManager = webResourceManager;
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

		WebResourceInfo resource = webResourceManager.getServerResource(name);
		if (resource == null) {
			return StringUtils.EMPTY;
		}
		
		String mimeType = resource.getMimeType();
		if (WebResourceManager.MIME_TYPE_JAVA_SCRIPT.contains(mimeType)){
			return String.format(SCRIPT_TEMPLATE, webResourceManager.getLocation(resource));
		}else if (WebResourceManager.MIME_TYPE_STYLE_SHEET.equals(mimeType)){
			return String.format(STYLESHEET_TEMPLATE, webResourceManager.getLocation(resource));
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
		
		WebResourceInfo resource = webResourceManager.getServerResource(resourceName);

		if (resource == null) {
			return StringUtils.EMPTY;
		}
		
		String location = webResourceManager.getLocation(resource);

		if (otherAttributes == null){
			otherAttributes = new LinkedHashMap<String, Object>();
		}

		otherAttributes.put(locationAttributeName, location);
		return write(tagName, otherAttributes, closeTag);
	}
	
}
