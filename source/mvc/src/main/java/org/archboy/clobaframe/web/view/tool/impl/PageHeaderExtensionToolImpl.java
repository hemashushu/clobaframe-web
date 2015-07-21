package org.archboy.clobaframe.web.view.tool.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.web.view.tool.context.ContextPageHeaderTool;
import org.archboy.clobaframe.web.view.tool.context.ContextPageHeaderProvider;
import org.archboy.clobaframe.web.view.tool.PageHeaderTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * In the none Spring MVC web application, it should invoke these each request:
 * 
 * HttpServletRequest httpServletRequest = new MockHttpServletRequest();
 * RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));
 * 
 * 
 * @author yang
 */
@Named
public class PageHeaderExtensionToolImpl implements ContextPageHeaderTool {

	@Inject
	private PageHeaderTool pageHeaderTool;
	
	@Autowired(required = false)
	private List<ContextPageHeaderProvider> pageHeaderProviders; // = new ArrayList<PageHeaderProvider>();
	
	private static final String customHeaderRequestAttributeName = "clobaframe-web-view.customPageHeaders";

	/*
	private static final ThreadLocal<HeaderContext> headerContextHolder = new ThreadLocal<HeaderContext>();
	
	public static class HeaderContext {
		private List<String> headers;

		public HeaderContext() {
			headers = new ArrayList<String>();
		}
		
		public void add(String header) {
			headers.add(header);
		}

		public List<String> getHeaders() {
			return headers;
		}
	}

	public static void clearContext() {
		headerContextHolder.remove();
	}
	
	public static HeaderContext getContext(){
		HeaderContext context = headerContextHolder.get();
		if (context == null) {
			context = createContext();
			headerContextHolder.set(context);
		}
		return context;
	}
	
	public static void setContext(HeaderContext headerContext) {
		headerContextHolder.set(headerContext);
	}
	
	public static HeaderContext createContext() {
		return new HeaderContext();
	}
	*/
	
	public void setPageHeaderProviders(List<ContextPageHeaderProvider> pageHeaderProviders) {
		this.pageHeaderProviders = pageHeaderProviders;
	}

	public void setPageHeaderTool(PageHeaderTool pageHeaderTool) {
		this.pageHeaderTool = pageHeaderTool;
	}

	@Override
	public void addProvider(ContextPageHeaderProvider pageHeaderProvider) {
		if (pageHeaderProviders == null) {
			pageHeaderProviders = new ArrayList<>();
		}
		
		pageHeaderProviders.add(pageHeaderProvider);
	}

	@Override
	public void removeProvider(String providerName) {
		Assert.notNull(providerName);
		
		for (int idx = pageHeaderProviders.size() - 1; idx >= 0; idx--){
			ContextPageHeaderProvider provider = pageHeaderProviders.get(idx);
			if (providerName.equals(provider.getName())){
				pageHeaderProviders.remove(idx);
				break;
			}
		}
	}

	@Override
	public void add(String tagName, Map<String, Object> attributes, boolean closeTag) {
		
		String header = pageHeaderTool.write(tagName, attributes, closeTag);
		
		// get custom header from current HTTP request attributes.
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		
		@SuppressWarnings("unchecked")
		List<String> extraHeaders = (List<String>)requestAttributes.getAttribute(
				customHeaderRequestAttributeName, RequestAttributes.SCOPE_REQUEST);
		
		if (extraHeaders == null) {
			extraHeaders = new ArrayList<String>();
			
			// set custom header to current HTTP request attributes.
			requestAttributes.setAttribute(customHeaderRequestAttributeName, 
				extraHeaders, 
				RequestAttributes.SCOPE_REQUEST);
		}
		
		extraHeaders.add(header);
		
//		HeaderContext headerContext = getContext();
//		headerContext.add(header);
	}
	
	@Override
	public List<String> list() {
		List<String> headers = new ArrayList<String>();
		
		if (pageHeaderProviders != null && !pageHeaderProviders.isEmpty()) {
			for(ContextPageHeaderProvider pageHeaderProvider : pageHeaderProviders){
				headers.addAll(pageHeaderProvider.list());
			}
		}
		
		// get custom header from current HTTP request attributes (HTTP request scope).
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		
		@SuppressWarnings("unchecked")
		List<String> extraHeaders = (List<String>)requestAttributes.getAttribute(
				customHeaderRequestAttributeName, RequestAttributes.SCOPE_REQUEST);

//		HeaderContext headerContext = getContext();
//		List<String> extraHeaders = headerContext.getHeaders();
		
		if (extraHeaders != null && !extraHeaders.isEmpty()){
			headers.addAll(extraHeaders);
		}
		
		return headers;
	}

	@Override
	public String write() {
		return write(null);
	}

	@Override
	public String write(String seperator) {
		List<String> headers = list();
		if (headers.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		return StringUtils.join(headers, seperator);
	}
	
}
