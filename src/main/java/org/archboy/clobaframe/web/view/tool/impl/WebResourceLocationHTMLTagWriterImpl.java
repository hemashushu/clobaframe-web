package org.archboy.clobaframe.web.view.tool.impl;

import org.archboy.clobaframe.web.view.tool.WebResourceLocationHTMLTagWriter;
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
public class WebResourceLocationHTMLTagWriterImpl implements WebResourceLocationHTMLTagWriter {

	private static final String MIME_TYPE_APPLICATION_JAVASCRIPT = "application/javascript";
	private static final String MIME_TYPE_APPLICATION_X_JAVASCRIPT = "application/x-javascript";
	private static final String MIME_TYPE_TEXT_JAVASCRIPT = "text/javascript";
	private static final String MIME_TYPE_TEXT_CSS = "text/css";

	/* Specifying type attributes in these contexts is not necessary as
	 * HTML5 implies text/css and text/javascript as defaults.
	 */
	//private static final String SCIRPT_TEMPLATE = "<script src=\"%s\" type=\"text/javascript\"></script>";
	//private static final String STYLESHEEP_TEMPLATE = "<link href=\"%s\" rel=\"stylesheet\" type=\"text/css\"/>";

	private static final String SCIRPT_TEMPLATE = "<script src=\"%s\"></script>";
	private static final String STYLESHEEP_TEMPLATE = "<link href=\"%s\" rel=\"stylesheet\"/>";

	@Inject
	private WebResourceManager webResourceManager;

	@Override
	public String write(String name) {
		String result = null;
		try{
			WebResourceInfo resource = webResourceManager.getResource(name);
			
			String contentType = resource.getContentType();
			if (contentType.equals(MIME_TYPE_APPLICATION_JAVASCRIPT) ||
					contentType.equals(MIME_TYPE_APPLICATION_X_JAVASCRIPT) ||
					contentType.equals(MIME_TYPE_TEXT_JAVASCRIPT)){
				result = String.format(SCIRPT_TEMPLATE, webResourceManager.getLocation(resource));
			}else if (contentType.equals(MIME_TYPE_TEXT_CSS)){
				result = String.format(STYLESHEEP_TEMPLATE, webResourceManager.getLocation(resource));
			}
		}catch(FileNotFoundException e){
			//
		}

		return result;
	}

	@Override
	public List<String> write(Collection<String> names) {
		List<String> results = new ArrayList<String>();

		for(String name : names){
			String result = write(name);
			if (result != null){
				results.add(result);
			}
		}

		return results;
	}
}
