package org.archboy.clobaframe.web.doc;

import java.util.Collection;
import java.util.Locale;

/**
 *
 * @author yang
 */
public interface DocProvider {
	
	public static final int PRIORITY_HIGHEST = 0;
	public static final int PRIORITY_HIGHER = 2;
	public static final int PRIORITY_HIGH = 4;
	public static final int PRIORITY_NORMAL = 5;
	
	/**
	 * Get the priority of the current provider.
	 * The higher priority provider will be check first when get a doc by
	 * name.
	 * I.E. when there are several doc that with the same name and locale, the doc
	 * that resist in the higher priority provider will be selected.
	 * 
	 * @return 
	 */
	int getPriority();
	
	/**
	 * 
	 * @return 
	 */
	Collection<Doc> getAll();
}
