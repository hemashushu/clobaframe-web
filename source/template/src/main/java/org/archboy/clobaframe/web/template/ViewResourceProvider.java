package org.archboy.clobaframe.web.template;

import org.archboy.clobaframe.io.NamedResourceInfo;
import org.springframework.core.Ordered;

/**
 *
 * @author yang
 */
public interface ViewResourceProvider extends Ordered {
	
	public static final int PRIORITY_HIGHEST = 0;
	public static final int PRIORITY_HIGHER = 20;
	public static final int PRIORITY_HIGH = 40;
	public static final int PRIORITY_NORMAL = 60;
	public static final int PRIORITY_LOW = 80;
	public static final int PRIORITY_LOWER = 100;
	
	String getName();
	
	NamedResourceInfo get(String name);
	
}
