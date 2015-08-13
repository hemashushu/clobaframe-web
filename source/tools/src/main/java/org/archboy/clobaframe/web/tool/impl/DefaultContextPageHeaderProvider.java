package org.archboy.clobaframe.web.tool.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.web.tool.ContextPageHeaderProvider;
import org.archboy.clobaframe.web.tool.PageHeaderTool;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * In the none Spring MVC web application, it should clear the 
 * RequestContextHolder each request:
 * 
 * HttpServletRequest httpServletRequest = ...
 * RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));
 * 
 * Or use the RequestContextListener or Filter etc.
 * 
 * @author yang
 */
@Named("contextPageHeaderProvider")
public class DefaultContextPageHeaderProvider implements ContextPageHeaderProvider {

	public static final String DEFAULT_PAGE_HEADER_REQUEST_ATTRIBUTE_NAME = "clobaframe.web.tool.pageHeaders";
	
	private String pageHeaderRequestAttributeName = DEFAULT_PAGE_HEADER_REQUEST_ATTRIBUTE_NAME;

	@Inject
	private PageHeaderTool pageHeaderTool;

	public void setPageHeaderTool(PageHeaderTool pageHeaderTool) {
		this.pageHeaderTool = pageHeaderTool;
	}

	public void setPageHeaderRequestAttributeName(String pageHeaderRequestAttributeName) {
		this.pageHeaderRequestAttributeName = pageHeaderRequestAttributeName;
	}

	@Override
	public int getOrder() {
		return PRIORITY_LOWER;
	}
	
	@Override
	public String getName() {
		return "defaultContextPageHeader";
	}
	
	@Override
	public void add(String tagName, Map<String, Object> attributes, boolean closeTag) {
		
		String header = pageHeaderTool.write(tagName, attributes, closeTag);
		
		// get custom header from current HTTP request attributes.
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		
		@SuppressWarnings("unchecked")
		List<String> extraHeaders = (List<String>)requestAttributes.getAttribute(
				pageHeaderRequestAttributeName, RequestAttributes.SCOPE_REQUEST);
		
		if (extraHeaders == null) {
			extraHeaders = new ArrayList<String>();
		}
		
		extraHeaders.add(header);
		
		// set custom header to current HTTP request attributes.
		requestAttributes.setAttribute(pageHeaderRequestAttributeName, 
				extraHeaders,
				RequestAttributes.SCOPE_REQUEST);
	}
	
	@Override
	public Collection<String> list() {
	
		// get custom header from current HTTP request attributes (HTTP request scope).
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		
		@SuppressWarnings("unchecked")
		List<String> extraHeaders = (List<String>)requestAttributes.getAttribute(
				pageHeaderRequestAttributeName, RequestAttributes.SCOPE_REQUEST);
		
		return (extraHeaders == null) ? new ArrayList<String>() : extraHeaders;

	}
	
}
