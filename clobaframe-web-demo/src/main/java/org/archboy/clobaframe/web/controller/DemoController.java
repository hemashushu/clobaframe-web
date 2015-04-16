package org.archboy.clobaframe.web.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.archboy.clobaframe.io.ResourceInfo;
import org.archboy.clobaframe.io.TemporaryResources;
import org.archboy.clobaframe.io.http.MultipartFormResourceInfo;
import org.archboy.clobaframe.io.http.MultipartFormResourceReceiver;
import org.archboy.clobaframe.io.http.ResourceSender;
import org.archboy.clobaframe.io.impl.DefaultTemporaryResources;
import org.archboy.clobaframe.media.Media;
import org.archboy.clobaframe.media.MediaFactory;
import org.archboy.clobaframe.media.image.Image;
import org.archboy.clobaframe.media.image.Imaging;
import org.archboy.clobaframe.media.image.OutputSettings;
import org.archboy.clobaframe.media.image.Transform;
import org.archboy.clobaframe.query.ViewModel;
import org.archboy.clobaframe.web.domain.UserContent;
import org.archboy.clobaframe.web.service.UserContentService;
import org.archboy.clobaframe.web.view.tool.PageHeaderTool;
import org.springframework.beans.propertyeditors.CurrencyEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@Inject
	private UserContentService userContentService;
	
	@Inject
	private ResourceSender resourceSender;
	
	@Inject
	private MediaFactory mediaFactory;
	
	@Inject
	private Imaging imaging;
	
	@Inject
	private PageHeaderTool pageHeaderTool;
	
	@RequestMapping("/")
	public String index(Model model){
		
		// add custom page header
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("href", "/atom.xml");
		attributes.put("type", "application/atom+xml");
		attributes.put("rel", "alternate");
		attributes.put("title", "Clobaframe-web ATOM Feed");
		pageHeaderTool.addHeader("link", attributes);
		
		// add model - current time
		model.addAttribute("now", new Date());
		model.addAttribute("html", "text contains <strong>html tags</strong>, 'single quotes' & \"double quotes\".");
		model.addAttribute("currency", 1234.567D);
		
		// add model - an object
		Map<String, String> viewObject = new HashMap<String, String>();
		viewObject.put("id", "001");
		viewObject.put("name", "foo");
		
		model.addAttribute("viewObject", viewObject);
		
		return "index";
	}
	
	@ResponseBody
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public UserContent createUpload(HttpServletRequest request) throws IOException{
		String contentType = request.getContentType();
		
		if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)){
			throw new IllegalArgumentException("Unsupport image type: " + contentType);
		}
		
		TemporaryResources temporaryResources = new DefaultTemporaryResources();
		try{
			InputStream in = request.getInputStream();
			Image image = (Image)mediaFactory.make(in, contentType, new Date(), temporaryResources);
			
			if (image.getWidth() > 1024 || image.getHeight() > 1024){
				Transform resizeTransform = imaging.resize(1024, 1024);
				image = imaging.apply(image, resizeTransform);
			}
			
			ResourceInfo resourceInfo = image.getResourceInfo(null, new OutputSettings(OutputSettings.OutputEncoding.JPEG, 92));
			
			// fix the Tomcat default url encoding that with ISO-8859-1
			// convert it into UTF-8 encoding. or set the URIEncoding="UTF-8 in the <Connector/>
			// see: http://wiki.apache.org/tomcat/FAQ/CharacterEncoding
			// String tagNameUTF8 = new String(tagName.getBytes("ISO_8859_1"), "UTF-8");
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			
			return userContentService.create(resourceInfo, title, description);
			
		}finally{
			temporaryResources.close();
		}
	}
	
	@RequestMapping(value = "/upload/{id}", params = {"data"}, method = RequestMethod.GET)
	public void getUpload(@PathVariable("id") String id, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		ResourceInfo resourceInfo = userContentService.getData(id);
		if (resourceInfo == null) {
			throw new FileNotFoundException();
		}
		
		resourceSender.send(resourceInfo, null, request, response);
	}
	
	@ResponseBody
	@RequestMapping(value = "/upload/{id}", method = RequestMethod.DELETE)
	public void deleteUpload(@PathVariable("id") String id) {
		userContentService.delete(id);
	}
	
	@ResponseBody
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public Collection<UserContent> listUpload(){
		return userContentService.list();
	}
	
//	@ResponseBody
//	@RequestMapping(value = "/ajax", method = RequestMethod.GET)
//	public Map<String, String> ajax(){
//		Map<String, String> user = new HashMap<String, String>();
//		user.put("id", "002");
//		user.put("name", "bar");
//		
//		return user;
//	}
//	
//	@ResponseBody
//	@RequestMapping(value = "/ajax", method = RequestMethod.POST)
//	public Map<String, Integer> ajaxPost(@RequestParam("a") int a, @RequestParam("b") int b){
//		int value = a + b;
//		Map<String, Integer> result = new HashMap<String, Integer>();
//		result.put("a", a);
//		result.put("b", b);
//		result.put("value", value);
//		
//		return result;
//	}
	
	@RequestMapping("/changelocale")
	public String changeLocale(HttpServletRequest request) {
		return "redirect:/";
	}
	
	@RequestMapping("/error/page-not-found")
	public String pageNotFound(){
		return "page-not-found";
	}
}
