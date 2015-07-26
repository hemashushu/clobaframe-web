package org.archboy.clobaframe.web.template.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import org.archboy.clobaframe.common.collection.DefaultObjectMap;
import org.archboy.clobaframe.common.collection.ObjectMap;
import org.archboy.clobaframe.web.tool.ContextPageHeaderProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author yang
 */
@Controller
public class VelocityController {
	
	@Inject
	private ContextPageHeaderProvider contextPageHeaderProvider;
	
	@RequestMapping("/index")
	public String index(Model model, Locale locale){
		
		// for context page header test
		ObjectMap attributes = new DefaultObjectMap()
			.add("href", "/atom.xml")
			.add("type", "application/atom+xml")
			.add("rel", "alternate")
			.add("title", "Clobaframe-web ATOM Feed");
		contextPageHeaderProvider.add("link", attributes, false);
		
		// for i18n test
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2015, 6, 10, 18, 5, 30);
		
		model.addAttribute("locale", locale.toLanguageTag());
		model.addAttribute("now", calendar.getTime());
		model.addAttribute("html", "text contains <strong>html tags</strong>, 'single quotes' & \"double quotes\".");
		model.addAttribute("htmlP", "line one.\nline two.");
		
		// for json writer test
		Map<String, String> viewObject = new HashMap<String, String>();
		viewObject.put("id", "001");
		viewObject.put("name", "foo");
		model.addAttribute("viewObject", viewObject);
		
		return "index";
	}
}
