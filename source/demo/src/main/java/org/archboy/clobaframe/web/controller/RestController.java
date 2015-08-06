package org.archboy.clobaframe.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.web.controller.form.PagePostForm;
import org.archboy.clobaframe.web.page.PageInfo;
import org.archboy.clobaframe.web.page.PageKey;
import org.archboy.clobaframe.web.page.PageManager;
import org.archboy.clobaframe.web.page.revision.RevisionPageManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author yang
 */
@Controller
public class RestController {
	
	@Inject
	private RevisionPageManager revisionPageManager;

	public void setRevisionPageManager(RevisionPageManager revisionPageManager) {
		this.revisionPageManager = revisionPageManager;
	}
	
	@ResponseBody
	@RequestMapping("/rest/page")
	public Collection<PageInfo> getPages(Locale locale,
			@RequestParam(value = "locale", required = false) Locale preferLocale) {
		
		if (preferLocale != null) {
			locale = preferLocale;
		}
		
		return listCompatibleLocalePage(locale, true);
	}

	@ResponseBody
	@RequestMapping(value = "/rest/page", method = RequestMethod.POST)
	public PageInfo createPage(
			@Valid PagePostForm form,
			BindingResult bindingResult){
		return revisionPageManager.save(new PageKey(form.getName(), form.getLocale()),
				form.getTitle(), form.getContent(), form.getUrlName(), null, null, null, null);
	}
	
	private Collection<PageInfo> listCompatibleLocalePage(Locale locale, boolean fallbackDefault) {
		
		Locale defaultLocale = revisionPageManager.getDefaultLocale();
		
		if (locale == null) {
			return revisionPageManager.listByLocale(defaultLocale);
		}
		
		Collection<PageInfo> pageInfos = revisionPageManager.listByLocale(locale);
		
		if (!pageInfos.isEmpty()) {
			return pageInfos;
		}
		
		// try to get the page without country code
		if (StringUtils.isNotEmpty(locale.getCountry())){

			pageInfos = revisionPageManager.listByLocale(
					new Locale(locale.getLanguage()));

			if (!pageInfos.isEmpty()) {
				return pageInfos;
			}
		}
			
			// try to get the page with default locale
		if (fallbackDefault) {
			return revisionPageManager.listByLocale(defaultLocale);
		}
		
		return new ArrayList<>();
	}

	
}
