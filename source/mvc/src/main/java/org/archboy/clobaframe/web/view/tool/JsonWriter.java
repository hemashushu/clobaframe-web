package org.archboy.clobaframe.web.view.tool;

/**
 * Write object to script JSON string.
 * 
 * @author yang
 */
public interface JsonWriter {

	/**
	 * 
	 * @param obj
	 * @return JSON string such as {id:'xxx', name:'yyy'}.
	 * Return EMPTY string when the object is null.
	 */
	String write(Object obj);

}
