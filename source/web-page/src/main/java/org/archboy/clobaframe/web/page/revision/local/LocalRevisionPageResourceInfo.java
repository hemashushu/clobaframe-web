package org.archboy.clobaframe.web.page.revision.local;

import org.archboy.clobaframe.io.ResourceInfo;
import org.archboy.clobaframe.io.file.FileBaseResourceInfo;

/**
 *
 * @author yang
 */
public interface LocalRevisionPageResourceInfo extends ResourceInfo {
	
	/**
	 * The page resource name, include relative path.
	 * E.g. "index.md", "devel/main.md".
	 * 
	 * @return
	 */
	String getName();
}
