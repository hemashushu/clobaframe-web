package org.archboy.clobaframe.web.controller;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import org.archboy.clobaframe.web.page.PageInfo;
import org.archboy.clobaframe.web.page.PageManager;
import org.archboy.clobaframe.web.page.revision.RevisionPageManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author yang
 */
@Controller
public class RestDemoController {
	
	@Inject
	private RevisionPageManager revisionPageManager;
	
	@ResponseBody
	@RequestMapping("/rest/page")
	public Collection<PageInfo> getPages(Locale locale,
			@RequestParam(value = "locale", required = false) Locale preferLocale) {
		
		if (preferLocale != null) {
			locale = preferLocale;
		}
		
		Collection<PageInfo> pageInfos = revisionPageManager.listByLocale(locale);
		return pageInfos;
	}

	
	
}
