package org.archboy.clobaframe.web.view.tool.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Named;
import org.archboy.clobaframe.web.view.tool.PageHeaderProvider;
import org.archboy.clobaframe.web.view.tool.PageHeaderTool;
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
		
		// get custom header from HTTP request attributes.
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
	public void addHeader(String tag, Map<String, String> attributes) {
		String header = writeHeader(tag, attributes);
		
		// get custom header from HTTP request attributes.
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		
		@SuppressWarnings("unchecked")
		List<String> extraHeaders = (List<String>)requestAttributes.getAttribute(
				customHeaderRequestAttributeName, RequestAttributes.SCOPE_REQUEST);
		
		if (extraHeaders == null) {
			extraHeaders = new ArrayList<String>();
		}
		
		extraHeaders.add(header);
		
		requestAttributes.setAttribute(customHeaderRequestAttributeName, 
				extraHeaders, 
				RequestAttributes.SCOPE_REQUEST);
		
	}

	@Override
	public String writeHeader(String tag, Map<String, String> attributes) {
		StringBuilder builder = new StringBuilder();
		builder.append("<");
		builder.append(tag);
		
		for(Map.Entry<String, String> entry : attributes.entrySet()){
			builder.append(" ");
			builder.append(entry.getKey());
			builder.append("=\"");
			builder.append(entry.getValue());
			builder.append("\"");
		}
		
		builder.append(">");
		return builder.toString();
	}
	
}
