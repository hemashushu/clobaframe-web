package org.archboy.clobaframe.web.mvc.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.archboy.clobaframe.web.mvc.RequestRoute;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@RequestRoute("^/$")
	public String index(Model model){
		model.addAttribute("hello", "world");
		return "index";
	}
	
	@RequestRoute("^/modelAndView$")
	public ModelAndView modelAndView(){
		ModelAndView mv = new ModelAndView("modeAndView")
				.addObject("id", 123)
				.addObject("name", "foo");
		return mv;
	}
	
	@RequestRoute("^/writer$")
	public void writer(HttpServletResponse response, Writer writer) throws IOException{
		response.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
		writer.write("hello");
	}

	@RequestRoute("^/query$")
	public String query(Model model, 
			@RequestParam("id") String id,
			@RequestParam("name") String name) {
		
		model.addAttribute("id", id)
				.addAttribute("name", name);
		
		return "query";
	}
	
	@RequestRoute(value = "^/form$", method = RequestMethod.POST)
	public String form(Model model, 
			@RequestParam("id") String id,
			@RequestParam("name") String name) {
		
		model.addAttribute("id", id)
				.addAttribute("name", name);
		
		return "form";
	}
	
	@RequestRoute("^/path/(?<id>\\w+)$")
	public String path(Model model, 
			@PathVariable("id") String id){
		model.addAttribute("id", id);
		return "path";
	}
	
	@ResponseBody
	@RequestRoute("^/string$")
	public String string(HttpServletResponse response){
		response.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
		return "hello world";
	}
	
	@ResponseBody
	@RequestRoute("^/object$")
	public User object(){
		return new User(456, "foo", "bar");
	}
	
	@ResponseBody
	@RequestRoute(value="^/requestBody$", method = RequestMethod.PUT)
	public String requestBody(@RequestBody Map<String, Object> map,
			Model model) {
		if (map.get("id").equals(222) &&
				map.get("name").equals("bar")) {
			model.addAttribute("result", "success");
		}else{
			model.addAttribute("result", "fail");
		}
		return "requestBody";
	}
	
	@RequestRoute("^/exception$")
	public void exception() throws IOException {
		if (Boolean.TRUE) {
			throw new FileNotFoundException("test");
		}
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
