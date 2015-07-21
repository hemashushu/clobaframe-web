package org.archboy.clobaframe.web.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.archboy.clobaframe.query.DefaultObjectMap;
import org.archboy.clobaframe.query.ObjectMap;
import org.archboy.clobaframe.web.view.tool.context.ContextPageHeaderTool;
import org.archboy.clobaframe.web.view.tool.PageHeaderTool;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author yang
 */
@Controller
public class VelocityViewController {
	
	@Inject
	private ContextPageHeaderTool pageHeaderContext;
	
	@RequestMapping("/index")
	public String index(Model model){
		
		// add custom page header
		ObjectMap attributes = new DefaultObjectMap()
			.add("href", "/atom.xml")
			.add("type", "application/atom+xml")
			.add("rel", "alternate")
			.add("title", "Clobaframe-web ATOM Feed");
		pageHeaderContext.add("link", attributes, false);
		
		// add model - current time
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2015, 6, 10, 18, 5, 30);
		
		model.addAttribute("now", calendar.getTime());
		model.addAttribute("html", "text contains <strong>html tags</strong>, 'single quotes' & \"double quotes\".");
		model.addAttribute("htmlP", "line one.\nline two.");
		model.addAttribute("integer", 1234567);
		
		// add model - an object
		Map<String, String> viewObject = new HashMap<String, String>();
		viewObject.put("id", "001");
		viewObject.put("name", "foo");
		
		model.addAttribute("viewObject", viewObject);
		
		return "index";
	}
}
