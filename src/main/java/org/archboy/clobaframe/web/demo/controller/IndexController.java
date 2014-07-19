package org.archboy.clobaframe.web.demo.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author yang
 */
@Controller
public class IndexController {
	
	@RequestMapping("/")
	public String index(Model model){
		model.addAttribute("now", new Date());
		
		Map<String, String> user = new HashMap<String, String>();
		user.put("id", "001");
		user.put("name", "foo");
		
		model.addAttribute("user", user);
		
		return "index";
	}
	
	@RequestMapping("/changelocale")
	public String changeLocale(HttpServletRequest request) {
		return "redirect:/";
	}
}
