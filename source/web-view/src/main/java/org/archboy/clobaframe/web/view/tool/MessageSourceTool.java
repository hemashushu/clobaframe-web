package org.archboy.clobaframe.web.view.tool;

import java.util.List;

/**
 * I18n write tool.
 * 
 * @author yang
 */
public interface MessageSourceTool {

	/**
	 * Get the local i18n write.
	 * 
	 * Placeholder is allowed in the code, such as:
	 * 'Hello {0}, now is {1,date,long}.'
	 * About MessageFormat, see http://docs.oracle.com/javase/8/docs/api/java/text/MessageFormat.html
	 * 
	 * @param code
	 * @param args
	 * @return 
	 */
	String write(String code, Object... args);

	/**
	 * 
	 * @param code
	 * @param args
	 * @return 
	 */
	String write(String code, List<Object> args);

	/**
	 * Return current locale language tag.
	 * Such as 'en-US', 'zh-CN'.
	 * 
	 * @return 
	 */
	String getLocale();
}
