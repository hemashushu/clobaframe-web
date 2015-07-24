package org.archboy.clobaframe.web.mvc.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author yang
 */
@Controller
public class IndexController {
	
	@RequestMapping("^/$")
	public String index(Model model){
		model.addAttribute("hello", "world");
		return "index";
	}
	
	@RequestMapping("^/modelAndView$")
	public ModelAndView modelAndView(){
		ModelAndView mv = new ModelAndView("modeAndView")
				.addObject("id", 123)
				.addObject("name", "foo");
		return mv;
	}
	
	@RequestMapping("^/writer$")
	public void writer(HttpServletResponse response, Writer writer) throws IOException{
		response.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
		writer.write("hello");
	}

	@RequestMapping("^/query$")
	public String query(Model model, 
			@RequestParam("id") String id,
			@RequestParam("name") String name) {
		
		model.addAttribute("id", id)
				.addAttribute("name", name);
		
		return "query";
	}
	
	@RequestMapping(value = "^/form$", method = RequestMethod.POST)
	public String form(Model model, 
			@RequestParam("id") String id,
			@RequestParam("name") String name) {
		
		model.addAttribute("id", id)
				.addAttribute("name", name);
		
		return "form";
	}
	
	@RequestMapping("^/path/(?<id>\\w+)$")
	public String path(Model model, 
			@PathVariable("id") String id){
		model.addAttribute("id", id);
		return "path";
	}
	
	@ResponseBody
	@RequestMapping("^/string$")
	public String string(HttpServletResponse response){
		response.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
		return "hello world";
	}
	
	@ResponseBody
	@RequestMapping("^/object$")
	public User object(){
		return new User(456, "foo", "bar");
	}
	
	public static class User {
		private int id;
		private String name;
		
		@JsonIgnore
		private String address;

		public User(int id, String name, String address) {
			this.id = id;
			this.name = name;
			this.address = address;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getAddress() {
			return address;
		}
	}
}
