package org.archboy.clobaframe.web.view.tool;

/**
 * Write object to script JSON string.
 * 
 * @author yang
 */
public interface JsonObjectWriter {

	/**
	 * 
	 * @param obj
	 * @return such as {id:'xxx', name:'yyy'}.
	 */
	String write(Object obj);

}
