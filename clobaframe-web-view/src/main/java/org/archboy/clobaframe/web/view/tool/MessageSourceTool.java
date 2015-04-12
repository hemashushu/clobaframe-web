package org.archboy.clobaframe.web.view.tool;

import java.util.List;

/**
 * I18n message tool.
 * 
 * @author yang
 */
public interface MessageSourceTool {

	/**
	 * Get the local i18n message.
	 * 
	 * Placeholder is allowed in the code, such as:
	 * 'Hello {0}, now is {1,date,long}.'
	 * 
	 * @param code
	 * @param args
	 * @return 
	 */
	String message(String code, Object... args);

	/**
	 * 
	 * @param code
	 * @param args
	 * @return 
	 */
	String message(String code, List<Object> args);

	/**
	 * Return current locale language tag.
	 * Such as 'en-US', 'zh-CN'.
	 * 
	 * @return 
	 */
	String getLocale();
}
