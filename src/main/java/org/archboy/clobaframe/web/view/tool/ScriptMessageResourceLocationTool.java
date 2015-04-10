package org.archboy.clobaframe.web.view.tool;

import org.archboy.clobaframe.webresource.WebResourceInfo;

/**
 * Get the default and local i18n message script resource.
 *
 * Commonly the i18n message resource name as 'messages.js' and
 * 'messages_LOCALE_CODE.js'.
 *
 * @author yang
 */
public interface ScriptMessageResourceLocationTool {

	/**
	 *
	 * @return
	 */
	String getDefaultMessageName();

	/**
	 *
	 * @return
	 */
	String getDefaultMessageLocation();

	/**
	 * Return null if no local message resource.
	 *
	 * @return
	 */
	String getLocalMessageName();

	/**
	 * Return null if no local message resource.
	 *
	 * @return
	 */
	String getLocalMessageLocation();
}
