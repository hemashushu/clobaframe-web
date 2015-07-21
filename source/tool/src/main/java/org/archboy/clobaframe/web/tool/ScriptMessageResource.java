package org.archboy.clobaframe.web.tool;

/**
 * Get the default and local i18n message script resource.
 *
 * Commonly the i18n message resource name is "messages.js" for default 
 * and "messages_LOCALE_CODE.js" for local.
 *
 * @author yang
 */
public interface ScriptMessageResource {

	/**
	 * Get the application default message resource.
	 * 
	 * @return NULL when no default message resource.
	 */
	String getDefaultResourceName();


	/**
	 * Get the user local message resource.
	 * The result maybe as the same as the application default, e.g. the
	 * application default is 'en' and the user local is the 'en' either.
	 * 
	 * @return NULL when no local message resource.
	 */
	String getLocalResourceName();
}
