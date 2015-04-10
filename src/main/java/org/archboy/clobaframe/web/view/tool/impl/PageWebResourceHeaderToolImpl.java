package org.archboy.clobaframe.web.view.tool.impl;

import org.archboy.clobaframe.web.view.tool.PageWebResourceHeaderTool;
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
public class PageWebResourceHeaderToolImpl implements PageWebResourceHeaderTool {

	private static final String MIME_TYPE_APPLICATION_JAVASCRIPT = "application/javascript";
	private static final String MIME_TYPE_APPLICATION_X_JAVASCRIPT = "application/x-javascript";
	private static final String MIME_TYPE_TEXT_JAVASCRIPT = "text/javascript";
	private static final String MIME_TYPE_TEXT_CSS = "text/css";

	/* Specifying type attributes in these contexts is not necessary as
	 * HTML5 implies text/css and text/javascript as defaults.
	 */
	//private static final String SCIRPT_TEMPLATE = "<script src=\"%s\" type=\"text/javascript\"></script>";
	//private static final String STYLESHEEP_TEMPLATE = "<link href=\"%s\" rel=\"stylesheet\" type=\"text/css\"/>";

	private static final String SCRIPT_TEMPLATE = "<script src=\"%s\"></script>";
	private static final String STYLESHEET_TEMPLATE = "<link href=\"%s\" rel=\"stylesheet\">";
	//private static final String CUSTOM_TEMPLATE = "<%s %s>";
	
	@Inject
	private WebResourceManager webResourceManager;
	
	@Override
	public String getHeader(String name) {
		String result = null;
		try{
			WebResourceInfo resource = webResourceManager.getResource(name);
			
			String mimeType = resource.getMimeType();
			if (mimeType.equals(MIME_TYPE_APPLICATION_JAVASCRIPT) ||
					mimeType.equals(MIME_TYPE_APPLICATION_X_JAVASCRIPT) ||
					mimeType.equals(MIME_TYPE_TEXT_JAVASCRIPT)){
				result = String.format(SCRIPT_TEMPLATE, webResourceManager.getLocation(resource));
			}else if (mimeType.equals(MIME_TYPE_TEXT_CSS)){
				result = String.format(STYLESHEET_TEMPLATE, webResourceManager.getLocation(resource));
			}
		}catch(FileNotFoundException e){
			//
		}

		return result;
	}

	@Override
	public List<String> getHeaders(Collection<String> names) {
		List<String> results = new ArrayList<String>();

		for(String name : names){
			String result = getHeader(name);
			if (result != null){
				results.add(result);
			}
		}

		return results;
	}

//	@Override
//	public String getHeader(String name, Collection<String> nameValues) {
//		StringBuilder builder = new StringBuilder();
//		for(String nameValue : nameValues){
//			int pos = nameValue.indexOf('=');
//			if (pos > 0){
//				builder.append(' ');
//				builder.append(nameValue.substring(0, pos));
//				builder.append("=\"");
//				builder.append(nameValue.substring(pos+1));
//				builder.append('"');
//			}
//		}
//		
//		return String.format(CUSTOM_TEMPLATE, name, builder.toString());
//	}
}
