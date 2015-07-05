package org.archboy.clobaframe.web.controller;

import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.query.DefaultObjectMap;
import org.archboy.clobaframe.query.ObjectMap;
import org.archboy.clobaframe.setting.common.global.GlobalSetting;
import org.archboy.clobaframe.web.exception.NotFoundException;
import org.archboy.clobaframe.web.page.PageInfo;
import org.archboy.clobaframe.web.page.revision.RevisionPageManager;
import org.archboy.clobaframe.web.theme.ThemeManager;
import org.archboy.clobaframe.web.theme.ThemePackage;
import org.archboy.clobaframe.web.view.tool.PageHeaderExtensionTool;
import org.archboy.clobaframe.web.view.tool.ThemePageHeader;
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
public class DemoController {

	@Inject
	private GlobalSetting globalSetting;
	
	@Inject
	private ThemeManager themeManager;
	
	@Inject
	private RevisionPageManager pageManager;
	
	@Inject
	private ThemePageHeader themePageHeader;
	
//	@Inject
//	private NoteService noteService;
//	
//	@Inject
//	private CacheResourceSender cacheResourceSender;
//	
//	@Inject
//	private MediaFactory mediaFactory;
//	
//	@Inject
//	private Imaging imaging;
//	
//	@Inject
//	private PageHeaderExtensionTool pageHeaderContext;
	
	@RequestMapping("/")
	public String index(Locale locale, Model model){
		
		ObjectMap pageOptions = new DefaultObjectMap()
				.add("locale", locale);
		
		model.addAttribute("pageOptions", pageOptions);
		
		return "index";
	}
	
	

	
//	
//	@ResponseBody
//	@RequestMapping(value = "/note", method = RequestMethod.POST)
//	public Note createNote(HttpServletRequest request) throws IOException{
//		String contentType = request.getContentType();
//		
//		if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)){
//			throw new IllegalArgumentException("Unsupport image type: " + contentType);
//		}
//		
//		TemporaryResources temporaryResources = new DefaultTemporaryResources();
//		try{
//			InputStream in = request.getInputStream();
//			Image image = (Image)mediaFactory.make(in, contentType, new Date(), temporaryResources);
//			
//			if (image.getWidth() > 1024 || image.getHeight() > 1024){
//				Transform resizeTransform = imaging.resize(1024, 1024);
//				image = imaging.apply(image, resizeTransform);
//			}
//			
//			ResourceInfo resourceInfo = image.getResourceInfo(null, new OutputSettings(OutputSettings.OutputEncoding.JPEG, 92));
//			
//			// fix the Tomcat default url encoding that with ISO-8859-1
//			// convert it into UTF-8 encoding. or set the URIEncoding="UTF-8 in the <Connector/>
//			// see: http://wiki.apache.org/tomcat/FAQ/CharacterEncoding
//			// String tagNameUTF8 = new String(tagName.getBytes("ISO_8859_1"), "UTF-8");
//			String title = request.getParameter("title");
//			String description = request.getParameter("description");
//			
//			return noteService.create(resourceInfo, title, description);
//			
//		}finally{
//			temporaryResources.close();
//		}
//	}
//	
//	@ResponseBody
//	@RequestMapping(value = "/note/{id}", method = RequestMethod.GET)
//	public Note getNote(@PathVariable("id") String id) {
//		Note note = noteService.get(id);
//		if (note == null) {
//			throw new NotFoundException();
//		}
//		return note;
//	}
//	
//	@ResponseBody
//	@RequestMapping(value = "/note", method = RequestMethod.GET)
//	public Collection<Note> listNote(){
//		return noteService.list();
//	}
//	
//	@ResponseBody
//	@RequestMapping(value = "/note/{id}", method = RequestMethod.DELETE)
//	public void deleteNote(@PathVariable("id") String id) {
//		noteService.delete(id);
//	}
//	
//	@ResponseBody
//	@RequestMapping(value = "/note/{id}", method = RequestMethod.PUT)
//	public Note updateNote(@PathVariable("id") String id,
//			@Valid @RequestBody PageUpdateForm form, // @RequestBody for JSON post
//			BindingResult bindingResult
//			) {
//		
//		// check validation
//		if (bindingResult.hasErrors()){
//			throw new IllegalArgumentException("Form data error."); // should throws 400 - bad request
//		}
//		return noteService.update(id, form.getTitle(), form.getDescription());
//	}
//	
//	@RequestMapping(value = "/note/{id}/photo", params = {"data"}, method = RequestMethod.GET)
//	public void getPhoto(@PathVariable("id") String id, 
//			HttpServletRequest request, HttpServletResponse response) throws IOException {
//		
//		Note note = noteService.get(id);
//		if (note == null) {
//			throw new NotFoundException();
//		}
//		
//		ResourceInfo resourceInfo = noteService.getPhoto(note.getPhotoId());
//		if (resourceInfo == null) {
//			throw new NotFoundException();
//		}
//		
//		cacheResourceSender.send(resourceInfo, 
//				CacheResourceSender.CACHE_CONTROL_PUBLIC,
//				CacheResourceSender.ONE_MONTH_SECONDS,
//				null, request, response);
//	}
	
	@ResponseBody
	@RequestMapping("/setlanguage")
	public ObjectMap setlanguage(Locale locale) {
		// route for org.springframework.web.servlet.i18n.LocaleChangeInterceptor
		return new DefaultObjectMap()
				.add("result", "success")
				.add("locale", locale.toLanguageTag());
	}
	
	@ResponseBody
	@RequestMapping("/changetheme")
	public List<String> changeTheme(@RequestParam(value = "name", required = false, defaultValue = "") String name) {
		if (StringUtils.isNotEmpty(name)) {
			ThemePackage themePackage = themeManager.get(ThemeManager.PACKAGE_CATALOG_LOCAL, name);
			if (themePackage == null) {
				throw new NotFoundException("no this theme:" + name);
			}
		}
		
		globalSetting.set("theme", name);
		
//		return new DefaultViewModel()
//				.add("result", "success")
//				.add("theme", name);
		
		return themePageHeader.list(name);
	}
	
	@RequestMapping("/error")
	public String error(@RequestParam(value = "code", required = false, defaultValue = "") String code,
			Model model){
		model.addAttribute("code", code);
		return "error";
	}
}
