package org.archboy.clobaframe.web.view.tool.impl;

import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.tools.generic.EscapeTool;

/**
 *
 * @author yang
 */
@Named
public class ExtendedEscapeTool extends EscapeTool{
	
	/**
	 * Escape HTML and replace two or more new line symbols with one new line symbol "\n", and then
	 * replace all "\n" with &lt;p&gt; and &lt;/p&gt;.
	 * 
	 * No &lt;p&gt; tag will be append when the source text only one line.
	 * @param text
	 * @return EMPTY string when the source text is null.
	 */
	public String htmlP(Object text){
		if (StringUtils.isEmpty((String)text)) {
			return StringUtils.EMPTY;
		}
		
		String escapedText = html(text);
		escapedText = escapedText.replaceAll("\n+", "\n");
		
		String[] lines = escapedText.split("\n");
		
		if (lines.length == 1){
			return lines[0];
		}
		
		StringBuilder builder = new StringBuilder(lines.length * 3);
		for (String line : lines) {
			builder.append("<p>");
			builder.append(line);
			builder.append("</p>");
		}
		
		return builder.toString();
	}
	
	/**
	 * the original method will return NULL, it's not friendly to velocity.
	 * @param text
	 * @return 
	 */
	@Override
	public String html(Object text) {
		if (StringUtils.isEmpty((String)text)) {
			return StringUtils.EMPTY;
		}
		
		return super.html(text);
	}
	
	/**
	 * the original method will return NULL, it's not friendly to velocity.
	 * @param text
	 * @return 
	 */
	@Override
	public String javascript(Object text) {
		if (StringUtils.isEmpty((String)text)) {
			return StringUtils.EMPTY;
		}
		
		return super.javascript(text);
	}
	
	/**
	 * the original method will return NULL, it's not friendly to velocity.
	 * @param text
	 * @return 
	 */
	@Override
	public String url(Object text) {
		if (StringUtils.isEmpty((String)text)) {
			return StringUtils.EMPTY;
		}
		
		return super.url(text);
	}
}
