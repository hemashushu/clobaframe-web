package org.archboy.clobaframe.web.controller;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.archboy.clobaframe.webresource.WebResourceSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 *
 * Using Spring Web MVC template URL to catch all web resource request
 * that under "/resources/", then @RequestMapping("/resource/{resourceVersionName:.+}")
 *
 * @author yang
 */
@Controller
public class ResourcesController {

	@Inject
	private WebResourceSender webResourceSender;

	/**
	 * Send web resource.
	 *
	 * @param resourceVersionName
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/resource/{resourceVersionName:.+}")
	public void sendResource(@PathVariable("resourceVersionName") String resourceVersionName,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		webResourceSender.sendByVersionName(resourceVersionName, request, response);
	}

	@RequestMapping("/robots.txt")
	public void sendRebots(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		webResourceSender.send("root/robots.txt", request, response);
	}

	@RequestMapping("/favicon.ico")
	public void sendFavoriteIcon(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		webResourceSender.send("root/favicon-16x16.ico", request, response);
	}
	
	@RequestMapping("/favicon.png")
	public void sendFavoriteIconInPNG(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		webResourceSender.send("root/favicon-16x16.png", request, response);
	}
	
	@RequestMapping("/apple-touch-icon.png")
	public void sendAppleTouchIcon(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		webResourceSender.send("root/apple-touch-icon-120x120.png", request, response);
	}
	
	@RequestMapping("/launcher-icon-192x192.png")
	public void sendLauncherIcon(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		webResourceSender.send("root/launcher-icon-192x192.png", request, response);
	}
	
}
