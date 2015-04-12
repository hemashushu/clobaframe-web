package org.archboy.clobaframe.web.view.tool.impl;

import org.archboy.clobaframe.web.view.tool.WebResourcePageHeaderTool;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.webresource.WebResourceInfo;
import org.archboy.clobaframe.webresource.WebResourceManager;

/**
 *
 * @author yang
 */
@Named
public class WebResourcePageHeaderToolImpl implements WebResourcePageHeaderTool {

	/**
	 * Specifying type attributes in these contexts is not necessary as
	 * HTML5 implies text/css and text/javascript as defaults.
	 * i.e.
	 * type="text/javascript" and type="text/css" is not necessary in HTML5.
	 */

	private static final String SCRIPT_TEMPLATE = "<script src=\"%s\"></script>";
	private static final String STYLESHEET_TEMPLATE = "<link href=\"%s\" rel=\"stylesheet\">";
	
	@Inject
	private WebResourceManager webResourceManager;
	
	@Override
	public String writeHeader(String name) {
		String result = null;
		try{
			WebResourceInfo resource = webResourceManager.getResource(name);
			
			String mimeType = resource.getMimeType();
			if (WebResourceManager.MIME_TYPE_JAVA_SCRIPT.contains(mimeType)){
				result = String.format(SCRIPT_TEMPLATE, webResourceManager.getLocation(resource));
				
			}else if (WebResourceManager.MIME_TYPE_STYLE_SHEET.equals(mimeType)){
				result = String.format(STYLESHEET_TEMPLATE, webResourceManager.getLocation(resource));
			}
		}catch(FileNotFoundException e){
			// ignore
		}

		return result;
	}

	@Override
	public List<String> getHeaders(Collection<String> names) {
		List<String> results = new ArrayList<String>();

		for(String name : names){
			String result = writeHeader(name);
			if (result != null){
				results.add(result);
			}
		}

		return results;
	}
}
