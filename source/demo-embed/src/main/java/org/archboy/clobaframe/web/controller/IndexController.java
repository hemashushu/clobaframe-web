package org.archboy.clobaframe.web.controller;

import java.util.Locale;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.archboy.clobaframe.common.collection.DefaultObjectMap;
import org.archboy.clobaframe.common.collection.ObjectMap;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

/**
 *
 * @author yang
 */
@Controller
public class IndexController implements Ordered {

	@Inject
	private CookieLocaleResolver cookieLocaleResolver;
	
	@Override
	public int getOrder() {
		return 10; // higher priority for the index page '/'
	}
	
	@RequestMapping("^/$")
	public String index(Locale locale, Model model){
		ObjectMap pageOptions = new DefaultObjectMap()
				.add("locale", locale);
		
		model.addAttribute("pageOptions", pageOptions);
		
		return "index";
	}
	
	@ResponseBody
	@RequestMapping("^/setlanguage$")
	public ObjectMap setLanguage(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam("locale") Locale locale) {
		// route for org.springframework.web.servlet.i18n.LocaleChangeInterceptor
		
		cookieLocaleResolver.setLocale(request, response, locale);
		
		return new DefaultObjectMap()
				.add("locale", locale.toLanguageTag());
	}
	
	@RequestMapping("^/error$")
	public String error(
			@RequestParam(value = "code", required = false) String code,
			Model model){
		
		model.addAttribute("error", 
				new DefaultObjectMap()
						.add("code", code));
		return "error";
	}
}
