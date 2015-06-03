package org.archboy.clobaframe.web.page.revision.local;

import java.io.File;

/**
 * Generate the resource name.
 * 
 * @author yang
 */
public interface LocalPageResourceNameStrategy {
	
	String getName(File file);
	
}
