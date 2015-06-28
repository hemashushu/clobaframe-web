package org.archboy.clobaframe.web.controller;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.archboy.clobaframe.setting.global.GlobalSetting;
import org.archboy.clobaframe.webresource.WebResourceSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author yang
 */
@Controller
public class ThemeController {
	
	@Inject
	private GlobalSetting globalSetting;
	
	@RequestMapping("/changetheme")
	public String changeTheme(@RequestParam(value="name", required = false, defaultValue = "") String name){
		globalSetting.set("theme", name);
		return "index";
	}
}
