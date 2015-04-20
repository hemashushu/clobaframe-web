package org.archboy.clobaframe.web.view.tool.impl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.web.view.tool.PageHeaderProvider;
import org.archboy.clobaframe.web.view.tool.PageHeaderTool;
import org.archboy.clobaframe.webresource.WebResourceInfo;
import org.archboy.clobaframe.webresource.WebResourceManager;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 *
 * @author yang
 */
@Named
public class PageHeaderToolImpl implements PageHeaderTool {

	private List<PageHeaderProvider> pageHeaderProviders = new ArrayList<PageHeaderProvider>();
	
	private static final String customHeaderRequestAttributeName = "customPageHeaders";
	
	/**
	 * About the script and style sheet header template.
	 * Specifying type attributes in these contexts is not necessary as
	 * HTML5 implies text/css and text/javascript as defaults.
	 * i.e.
	 * type="text/javascript" and type="text/css" is not necessary in HTML5.
	 */

	private static final String SCRIPT_TEMPLATE = "<script src=\"%s\"></script>";
	private static final String STYLESHEET_TEMPLATE = "<link href=\"%s\" rel=\"stylesheet\">";
	
	@Inject
	private WebResourceManager webResourceManager;
	
	@Override
	public void addPageHeaderProvider(PageHeaderProvider pageHeaderProvider) {
		pageHeaderProviders.add(pageHeaderProvider);
	}

	@Override
	public List<String> getHeaders() {
		List<String> headers = new ArrayList<String>();
		for(PageHeaderProvider pageHeaderProvider : pageHeaderProviders){
			headers.addAll(pageHeaderProvider.getHeaders());
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
		String header = writeHeader(tagName, attributes, closeTag);
		
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
	public String writeHeader(String tagName, Map<String, Object> attributes, boolean closeTag) {
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
		try{
			WebResourceInfo resource = webResourceManager.getResource(name);
			
			String mimeType = resource.getMimeType();
			if (WebResourceManager.MIME_TYPE_JAVA_SCRIPT.contains(mimeType)){
				return String.format(SCRIPT_TEMPLATE, webResourceManager.getLocation(resource));
				
			}else if (WebResourceManager.MIME_TYPE_STYLE_SHEET.equals(mimeType)){
				return String.format(STYLESHEET_TEMPLATE, webResourceManager.getLocation(resource));
			}else{
				// unsupport resource type
				return null;
			}
			
		}catch(FileNotFoundException e){
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
	public String writeResource(String tagName, 
			String locationAttributeName, String resourceName, 
			Map<String, Object> otherAttributes,
			boolean closeTag) {
		try{
			WebResourceInfo resource = webResourceManager.getResource(resourceName);
			String location = webResourceManager.getLocation(resource);
			
			if (otherAttributes == null){
				otherAttributes = new HashMap<String, Object>();
			}
			
			otherAttributes.put(locationAttributeName, location);
			return writeHeader(tagName, otherAttributes, closeTag);
			
		}catch(FileNotFoundException e){
			return null;
		}
	}
	
}
