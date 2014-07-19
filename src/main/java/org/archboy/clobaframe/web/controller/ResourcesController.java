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
 * that under "/resources/web/", then @RequestMapping("/resource/web/{resourceUniqueName:.+}")
 *
 * @author yang
 */
@Controller
public class ResourcesController {

	@Inject
	private WebResourceSender webResourceSender;

	/**
	 * Send static web resource. only avaliable while web resource using 'local' strategy.
	 *
	 * @param resourceUniqueName
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/resource/{resourceUniqueName:.+}")
	public void sendResource(@PathVariable("resourceUniqueName") String resourceUniqueName,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		webResourceSender.sendByUniqueName(resourceUniqueName, request, response);
	}

	@RequestMapping("/robots.txt")
	public void sendRebots(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		webResourceSender.send("root/robots.txt", request, response);
	}

	@RequestMapping("/favicon.ico")
	public void sendFavicon(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		webResourceSender.send("root/logo-16x16.ico", request, response);
	}
}
