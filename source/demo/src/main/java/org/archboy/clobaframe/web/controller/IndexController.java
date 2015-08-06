package org.archboy.clobaframe.web.controller;

import java.util.Locale;
import org.archboy.clobaframe.common.collection.DefaultObjectMap;
import org.archboy.clobaframe.common.collection.ObjectMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author yang
 */
@Controller
public class IndexController {
	
	@RequestMapping("/")
	public String index(Locale locale, Model model){
		
		ObjectMap pageOptions = new DefaultObjectMap()
				.add("locale", locale);
		
		model.addAttribute("pageOptions", pageOptions);
		
		return "index";
	}
	
	@ResponseBody
	@RequestMapping("/setlanguage")
	public ObjectMap setLanguage(Locale locale) {
		// route for org.springframework.web.servlet.i18n.LocaleChangeInterceptor
		return new DefaultObjectMap()
				//.add("result", "success")
				.add("locale", locale.toLanguageTag());
	}
	
	@RequestMapping("/error")
	public String error(@RequestParam(value = "code", required = false, defaultValue = "") String code,
			Model model){
		model.addAttribute("code", code);
		return "error";
	}
}
