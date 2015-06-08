package org.archboy.clobaframe.web.controller;

import java.io.IOException;
import java.util.Locale;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.web.controller.exception.PageNotFound;
import org.archboy.clobaframe.web.page.PageInfo;
import org.archboy.clobaframe.web.page.PageKey;
import org.archboy.clobaframe.web.page.PageManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author yang
 */
@Controller
public class PageController {

	private final int pagePathLength = "/page/".length();

	private static final String DEFAULT_TEMPLATE_NAME = "share/page";
	
	@Value("${clobaframe.web.page.defaultTemplateName:" + DEFAULT_TEMPLATE_NAME + "}")
	private String defaultTemplateName;
	
	@Inject
	private PageManager pageManager;
	
	@RequestMapping("/page/**")
	public String sendPage(
			HttpServletRequest request,
			Locale locale,
			Model model) throws IOException {
		String path = request.getRequestURI();
		String pageName = path.substring(pagePathLength);
		
		PageInfo page = getCompatiblePage(pageName, locale);
		if (page == null){
			throw new PageNotFound(path);
		}
		
		model.addAttribute("page", page);
		
		String templateName = StringUtils.isEmpty(page.getTemplateName()) ? 
				defaultTemplateName : 
				page.getTemplateName();
		
		return templateName;
	}
	
	@RequestMapping("/**")
	public String sendUrlPage(
			HttpServletRequest request,
			Locale locale,
			Model model) throws IOException {
		String path = request.getRequestURI();
		String pathName = path.substring(1);
		String pageName = pageManager.getByUrlName(pathName);
		
		if (pageName == null) {
			throw new PageNotFound(path);
		}
		
		PageInfo page = getCompatiblePage(pageName, locale);
		if (page == null){
			throw new PageNotFound(path);
		}
		
		model.addAttribute("page", page);
		
		String templateName = StringUtils.isEmpty(page.getTemplateName()) ? 
				defaultTemplateName : 
				page.getTemplateName();
		
		return templateName;
	}

	/**
	 * try to find the compatible language page.
	 * 
	 * @param pageName
	 * @param locale
	 * @return 
	 */
	private PageInfo getCompatiblePage(String pageName, Locale locale) {
		
		PageKey pageKey = new PageKey(pageName, locale);
		PageInfo page = pageManager.get(pageKey);
		
		if (page != null) {
			return page;
		}
		
		if (StringUtils.isNotEmpty(locale.getLanguage())){
			
			// try to get the page without country code
			if (StringUtils.isNotEmpty(locale.getCountry())){
			
				locale = new Locale(locale.getLanguage());
				pageKey = new PageKey(pageName, locale);
				page = pageManager.get(pageKey);

				if (page != null) {
					return page;
				}
			}
			
			// try to get the page with default locale
			pageKey = new PageKey(pageName, pageManager.getDefaultLocale());
			page = pageManager.get(pageKey);
		}
		
		return page;
	}
		
}
