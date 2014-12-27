package org.archboy.clobaframe.web.view.tool;

import java.util.List;

/**
 *
 * @author yang
 */
public interface MessageSourceTool {

	/**
	 * Placeholder is allowed in the code, such as:
	 * 'Hello {0}, now is {1,date,long}.'
	 * 
	 * @param code
	 * @param args
	 * @return 
	 */
	String get(String code, Object... args);

	String get(String code, List<Object> args);

	/**
	 * Return current locale language tag.
	 * Such as 'en-US', 'zh-CN'.
	 * 
	 * @return 
	 */
	String getLocale();
}
