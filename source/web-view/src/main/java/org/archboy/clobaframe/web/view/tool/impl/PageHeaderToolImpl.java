package org.archboy.clobaframe.web.view.tool.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.web.view.tool.PageHeaderProvider;
import org.archboy.clobaframe.web.view.tool.PageHeaderTool;
import org.archboy.clobaframe.webresource.WebResourceInfo;
import org.archboy.clobaframe.webresource.WebResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 *
 * @author yang
 */
@Named
public class PageHeaderToolImpl implements PageHeaderTool {

	@Autowired(required = false)
	private List<PageHeaderProvider> pageHeaderProviders = new ArrayList<PageHeaderProvider>();
	
	private static final String customHeaderRequestAttributeName = "clobaframe-web-view.customPageHeaders";
	
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

	@Override
	public List<String> getHeaders() {
		List<String> headers = new ArrayList<String>();
		
		if (pageHeaderProviders != null && !pageHeaderProviders.isEmpty()) {
			for(PageHeaderProvider pageHeaderProvider : pageHeaderProviders){
				headers.addAll(pageHeaderProvider.getHeaders());
			}
		}
		
		// get custom header from current HTTP request attributes (HTTP request scope).
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		
		@SuppressWarnings("unchecked")
		List<String> extraHeaders = (List<String>)requestAttributes.getAttribute(
				customHeaderRequestAttributeName, RequestAttributes.SCOPE_REQUEST);
		
		if (extraHeaders != null && !extraHeaders.isEmpty()){
			headers.addAll(extraHeaders);
		}
		
		return headers;
	}

	@Override
	public void addHeader(String tagName, Map<String, Object> attributes, boolean closeTag) {
		String header = write(tagName, attributes, closeTag);
		
		// get custom header from current HTTP request attributes.
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		
		@SuppressWarnings("unchecked")
		List<String> extraHeaders = (List<String>)requestAttributes.getAttribute(
				customHeaderRequestAttributeName, RequestAttributes.SCOPE_REQUEST);
		
		if (extraHeaders == null) {
			extraHeaders = new ArrayList<String>();
		}
		
		extraHeaders.add(header);
		
		// set custom header to current HTTP request attributes.
		requestAttributes.setAttribute(customHeaderRequestAttributeName, 
				extraHeaders, 
				RequestAttributes.SCOPE_REQUEST);
		
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

		WebResourceInfo resource = webResourceManager.getResource(name);
		if (resource == null) {
			return null;
		}
		
		String mimeType = resource.getMimeType();
		if (WebResourceManager.MIME_TYPE_JAVA_SCRIPT.contains(mimeType)){
			return String.format(SCRIPT_TEMPLATE, webResourceManager.getLocation(resource));
		}else if (WebResourceManager.MIME_TYPE_STYLE_SHEET.equals(mimeType)){
			return String.format(STYLESHEET_TEMPLATE, webResourceManager.getLocation(resource));
		}else{
			// unsupport resource type
			return null;
		}
	}

	@Override
	public List<String> getResources(Collection<String> names) {
		List<String> results = new ArrayList<String>();

		for(String name : names){
			String result = writeResource(name);
			if (result != null){
				results.add(result);
			}
		}

		return results;
	}

	@Override
	public String writeResource(String resourceName, 
			String tagName, 
			String locationAttributeName, 
			Map<String, Object> otherAttributes,
			boolean closeTag) {
		
		WebResourceInfo resource = webResourceManager.getResource(resourceName);

		if (resource == null) {
			return null;
		}
		
		String location = webResourceManager.getLocation(resource);

		if (otherAttributes == null){
			otherAttributes = new LinkedHashMap<String, Object>();
		}

		otherAttributes.put(locationAttributeName, location);
		return write(tagName, otherAttributes, closeTag);
	}
	
}
