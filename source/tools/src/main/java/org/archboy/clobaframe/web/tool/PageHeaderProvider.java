package org.archboy.clobaframe.web.tool;

import java.util.Collection;
import org.springframework.core.Ordered;

/**
 * Add extra page header to {@link PageHeaderExtensionTool}.
 * 
 * @author yang
 */
public interface PageHeaderProvider extends Ordered {
	
	public static final int PRIORITY_HIGHEST = 0;
	public static final int PRIORITY_HIGHER = 20;
	public static final int PRIORITY_HIGH = 40;
	public static final int PRIORITY_NORMAL = 60;
	public static final int PRIORITY_LOW = 80;
	public static final int PRIORITY_LOWER = 100;
	
	/**
	 * Provider name.
	 * It's optional.
	 * 
	 * @return 
	 */
	String getName();
	
	/**
	 * 
	 * @return 
	 */
	Collection<String> list();
}
