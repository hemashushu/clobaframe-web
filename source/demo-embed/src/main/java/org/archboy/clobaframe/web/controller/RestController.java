package org.archboy.clobaframe.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.web.page.PageInfo;
import org.archboy.clobaframe.web.page.PageKey;
import org.archboy.clobaframe.web.page.revision.RevisionPageManager;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author yang
 */
@Controller
public class RestController implements Ordered {
	
	@Inject
	private RevisionPageManager revisionPageManager;

	public void setRevisionPageManager(RevisionPageManager revisionPageManager) {
		this.revisionPageManager = revisionPageManager;
	}

	@Override
	public int getOrder() {
		return 50;
	}
	
	@ResponseBody
	@RequestMapping("^/rest/page$")
	public Collection<PageInfo> getPages(Locale locale,
			@RequestParam(value = "locale", required = false) Locale preferLocale) {

		if (preferLocale != null) {
			locale = preferLocale;
		}
		
		return listCompatibleLocalePage(locale, true);
	}

	@ResponseBody
	@RequestMapping(value = "^/rest/page$", method = RequestMethod.POST)
	public PageInfo createPage(
			@RequestBody Map<String, Object> form){
		String formLocale = (String)form.get("locale");
		String name = (String)form.get("name");
		String title = (String)form.get("title");;
		String content = (String)form.get("content");;
		String urlName = (String)form.get("urlName");;
		
		Assert.hasText(formLocale);
		Assert.hasText(name);
		Assert.hasText(title);
		Assert.hasText(content);
		Assert.isTrue(StringUtils.isEmpty(urlName) || urlName.matches("^[a-zA-Z0-9\\.\\-\\/]+$"));
		
		Locale locale = Locale.forLanguageTag(formLocale);
		
		return revisionPageManager.save(
				new PageKey(name, locale),
				title, content, urlName, null, null, null, null);
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
