package org.archboy.clobaframe.web.tool.controller;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.archboy.clobaframe.resource.http.NamedResourceSender;
import org.springframework.stereotype.Controller;
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
	private NamedResourceSender namedResourceSender;
	
	private String baseLocation = "/resource/";
	private String rootResourceNamePrefix = "root/";
	
	/**
	 * Send web resource.
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	//@RequestMapping("/resource/{resourceVersionName:.+}")
	@RequestMapping("/resource/**")
	public void sendResource(
			//@PathVariable("resourceVersionName") String resourceVersionName,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String path = request.getRequestURI();
		int baseLocationLength = baseLocation.length();
		String resourceVersionName = path.substring(baseLocationLength);
		namedResourceSender.sendByVersionName(resourceVersionName, request, response);
	}

	@RequestMapping("/robots.txt")
	public void sendRebots(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		namedResourceSender.send(rootResourceNamePrefix + "robots.txt", request, response);
	}

	@RequestMapping("/favicon.ico")
	public void sendFavoriteIcon(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		namedResourceSender.send(rootResourceNamePrefix + "favicon-16x16.ico", request, response);
	}
	
	@RequestMapping("/favicon.png")
	public void sendFavoriteIconInPNG(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		namedResourceSender.send(rootResourceNamePrefix + "favicon-16x16.png", request, response);
	}
	
	@RequestMapping("/apple-touch-icon.png")
	public void sendAppleTouchIcon(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		namedResourceSender.send(rootResourceNamePrefix + "apple-touch-icon-120x120.png", request, response);
	}
	
	@RequestMapping("/launcher-icon-192x192.png")
	public void sendLauncherIcon(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		namedResourceSender.send(rootResourceNamePrefix + "launcher-icon-192x192.png", request, response);
	}
	
}
