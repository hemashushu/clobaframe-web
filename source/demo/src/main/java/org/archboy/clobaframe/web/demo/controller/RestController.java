package org.archboy.clobaframe.web.demo.controller;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import javax.validation.Valid;
import org.archboy.clobaframe.web.demo.controller.form.PagePostForm;
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
		
		Collection<PageInfo> pageInfos = revisionPageManager.listByLocale(locale);
		return pageInfos;
	}

	@ResponseBody
	@RequestMapping(value = "/rest/page", method = RequestMethod.POST)
	public PageInfo createPage(
			@Valid PagePostForm form,
			BindingResult bindingResult){
		return revisionPageManager.save(new PageKey(form.getName(), form.getLocale()),
				form.getTitle(), form.getContent(), form.getUrlName(), null, null, null, null);
	}
	
}
