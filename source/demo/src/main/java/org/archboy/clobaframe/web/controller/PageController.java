package org.archboy.clobaframe.web.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.query.simplequery.SimpleQuery;
import org.archboy.clobaframe.web.page.PageInfo;
import org.archboy.clobaframe.web.page.PageKey;
import org.archboy.clobaframe.web.page.revision.RevisionPageInfo;
import org.archboy.clobaframe.web.page.revision.RevisionPageManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author yang
 */
@Controller
public class PageController {

	private final int pagePathPrefixNameLength = "/page/".length();
	private final int pagePrefixUrlNameLength = "/".length();
	
//	private static final String DEFAULT_TEMPLATE_NAME = "page";
	
//	@Value("${clobaframe.web.page.defaultTemplateName:" + DEFAULT_TEMPLATE_NAME + "}")
//	private String defaultTemplateName;
	
	@Inject
	private RevisionPageManager revisionPageManager;
	
//	@Inject
//	private ObjectUrl objectUrl;

//	public void setDefaultTemplateName(String defaultTemplateName) {
//		this.defaultTemplateName = defaultTemplateName;
//	}

	public void setRevisionPageManager(RevisionPageManager revisionPageManager) {
		this.revisionPageManager = revisionPageManager;
	}

//	public void setObjectUrl(ObjectUrl objectUrl) {
//		this.objectUrl = objectUrl;
//	}
//	
//	@ModelAttribute
//	public void addModelAttribute(Model model) {
//		model.addAttribute("objectUrl", objectUrl);
//	}
	
	@RequestMapping("/page/**")
	public String sendPage(
			HttpServletRequest request,
			Locale locale,
			@RequestParam(value = "locale", required = false) Locale preferLocale,
			@RequestParam(value = "revision", required = false, defaultValue = "-1") int revision,
			Model model) throws IOException {
		
		String path = request.getRequestURI();
		String pageName = path.substring(pagePathPrefixNameLength);
		
		PageInfo page = null;
		if (revision >= 0) {
			Assert.notNull(preferLocale);
			page = revisionPageManager.get(new PageKey(pageName, preferLocale), revision);
		}else if (preferLocale != null) {
			page = revisionPageManager.get(new PageKey(pageName, preferLocale));
		}else {
			page = getCompatibleLocalePage(pageName, locale, true);
		}
		
		if (page == null){
			throw new FileNotFoundException(path);
		}
		
		Collection<RevisionPageInfo> revisions = revisionPageManager.listRevision(page.getPageKey());
		Collection<Locale> locales = revisionPageManager.listLocale(page.getPageKey().getName());
		
		revisions = SimpleQuery.from(revisions).orderBy("revision").list();
		
		model.addAttribute("page", page);
		model.addAttribute("revisions", revisions);
		model.addAttribute("locales", locales);
		
		String templateName = StringUtils.isEmpty(page.getTemplateName()) ? 
				revisionPageManager.getDefaultTemplateName(): 
				page.getTemplateName();
		
		return templateName;
	}
	
	@RequestMapping("/**")
	public String sendUrlPage(
			HttpServletRequest request,
			Locale locale,
			@RequestParam(value = "locale", required = false) Locale preferLocale,
			@RequestParam(value = "revision", required = false, defaultValue = "-1") int revision,
			Model model) throws IOException {
		
		String path = request.getRequestURI();
		String urlName = path.substring(pagePrefixUrlNameLength); // exclude the '/'.
		String pageName = revisionPageManager.getByUrlName(urlName);
		
		if (pageName == null) {
			throw new FileNotFoundException(path);
		}
		
		PageInfo page = null;
		if (revision >= 0) {
			Assert.notNull(preferLocale);
			page = revisionPageManager.get(new PageKey(pageName, preferLocale), revision);
		}else if (preferLocale != null) {
			page = revisionPageManager.get(new PageKey(pageName, preferLocale));
		}else {
			page = getCompatibleLocalePage(pageName, locale, true);
		}
		
		if (page == null){
			throw new FileNotFoundException(path);
		}
		
		Collection<RevisionPageInfo> revisions = revisionPageManager.listRevision(page.getPageKey());
		Collection<Locale> locales = revisionPageManager.listLocale(page.getPageKey().getName());
		
		revisions = SimpleQuery.from(revisions).orderBy("revision").list();
		
		model.addAttribute("page", page);
		model.addAttribute("revisions", revisions);
		model.addAttribute("locales", locales);
		
		String templateName = StringUtils.isEmpty(page.getTemplateName()) ? 
				revisionPageManager.getDefaultTemplateName(): 
				page.getTemplateName();
		
		return templateName;
	}

	/**
	 * try to find the compatible language page.
	 * 
	 * When specify locale is NULL then return the application default locale page.
	 * 
	 * @return 
	 */
	private PageInfo getCompatibleLocalePage(String pageName, Locale locale, boolean fallbackDefault) {
		
		Locale defaultLocale = revisionPageManager.getDefaultLocale();
		
		if (locale == null) {
			return revisionPageManager.get(new PageKey(pageName, defaultLocale));
		}
		
		PageInfo pageInfo = revisionPageManager.get(new PageKey(pageName, locale));
		
		if (pageInfo != null) {
			return pageInfo;
		}
		
		// try to get the page without country code
		if (StringUtils.isNotEmpty(locale.getCountry())){

			pageInfo = revisionPageManager.get(new PageKey(
					pageName, 
					new Locale(locale.getLanguage())));

			if (pageInfo != null) {
				return pageInfo;
			}
		}
			
			// try to get the page with default locale
		if (fallbackDefault) {
			return revisionPageManager.get(new PageKey(
					pageName, defaultLocale));
		}
		
		return null;
	}
		
}
