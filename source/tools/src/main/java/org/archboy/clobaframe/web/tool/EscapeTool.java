package org.archboy.clobaframe.web.tool;

/**
 *
 * @author yang
 */
public interface EscapeTool{
	
	/**
	 * Escape HTML and replace two or more new line symbols with one new line symbol "\n", and then
	 * replace all "\n" with &lt;p&gt; and &lt;/p&gt;.
	 * 
	 * No &lt;p&gt; tag will be append when the source text only one line.
	 * @param text
	 * @return EMPTY string when the source text is null.
	 */
	String htmlP(String text);
	
	/**
	 * 
	 * @param text
	 * @return EMPTY string when the source text is null.
	 */
	String html(String text);
	
	/**
	 * 
	 * @param text
	 * @return EMPTY string when the source text is null.
	 */
	String javascript(String text);
	
	/**
	 * 
	 * @param text
	 * @return EMPTY string when the source text is null.
	 */
	String url(String text);
}
