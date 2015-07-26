package org.archboy.clobaframe.web.tool.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.inject.Named;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.web.tool.EscapeTool;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 *
 * @author yang
 */
@Named("escapeTool")
public class EscapeToolImpl implements EscapeTool{
	
	@Override
	public String htmlP(String text){
		if (StringUtils.isEmpty(text)) {
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
	
	@Override
	public String html(String text) {
		if (StringUtils.isEmpty(text)) {
			return StringUtils.EMPTY;
		}
		
		return StringEscapeUtils.escapeHtml4(text);
	}
	
	@Override
	public String javascript(String text) {
		if (StringUtils.isEmpty(text)) {
			return StringUtils.EMPTY;
		}
		
		return StringEscapeUtils.escapeEcmaScript(text);
	}
	
	@Override
	public String url(String text) {
		if (StringUtils.isEmpty(text)) {
			return StringUtils.EMPTY;
		}
		
		try {
			return URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			return StringUtils.EMPTY;
		}
	}
}
