package org.archboy.clobaframe.web.view.tool;

/**
 * Get the default and local i18n message script resource.
 *
 * Commonly the i18n message resource name is "messages.js" for default 
 * and "messages_LOCALE_CODE.js" for local.
 *
 * @author yang
 */
public interface ScriptMessageResourceTool {

	/**
	 *  Return null if no default message resource.
	 * @return
	 */
	String getDefaultResourceName();


	/**
	 * Return null if no local message resource.
	 *
	 * @return
	 */
	String getLocalResourceName();
}
