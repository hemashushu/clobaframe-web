package org.archboy.clobaframe.web.view.tool.impl;

import javax.inject.Named;
import org.apache.velocity.tools.generic.EscapeTool;

/**
 *
 * @author yang
 */
@Named
public class ExtendedEscapeTool extends EscapeTool{
	public String htmlP(Object text){
		String escapedText = html(text);
		escapedText = escapedText.replaceAll("\n\n", "<p>");
		return escapedText.replaceAll("\n", "<br/>");
	}
}
