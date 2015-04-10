package org.archboy.clobaframe.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author yang
 */
@Controller
public class DemoController {
	
	@RequestMapping("/")
	public String index(Model model){
		model.addAttribute("now", new Date());
		
		Map<String, String> user = new HashMap<String, String>();
		user.put("id", "001");
		user.put("name", "foo");
		
		model.addAttribute("user", user);
		
		return "index";
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax", method = RequestMethod.GET)
	public Map<String, String> ajax(){
		Map<String, String> user = new HashMap<String, String>();
		user.put("id", "002");
		user.put("name", "bar");
		
		return user;
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax", method = RequestMethod.POST)
	public Map<String, Integer> ajaxPost(@RequestParam("a") int a, @RequestParam("b") int b){
		int value = a + b;
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("a", a);
		result.put("b", b);
		result.put("value", value);
		
		return result;
	}
	
	@RequestMapping("/changelocale")
	public String changeLocale(HttpServletRequest request) {
		return "redirect:/";
	}
}
