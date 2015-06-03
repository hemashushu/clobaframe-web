package org.archboy.clobaframe.web.page.revision.local;

import java.io.File;
import org.archboy.clobaframe.webresource.local.LocalWebResourceNameStrategy;

/**
 * Return resource name by file relate to the base path.
 * 
 * By default, the resource name is the file name that excludes the base folder path.
 * 
 * e.g. base folder is "/var/lib/clobaframe", then the file
 * "/var/lib/clobaframe/common.css" gets name "common.css",
 * and
 * "/var/lib/js/moments.js" gets name "js/moments.js".
 * 
 * @author yang
 */
public class DefaultLocalPageResourceNameStrategy implements LocalPageResourceNameStrategy {
	
	private int basePathLength;

	public DefaultLocalPageResourceNameStrategy(File basePath) {
		// the base path length plus 1 to exclude the resource file name path
		// '/' prefix character.
		this.basePathLength = basePath.getPath().length() + 1; 
	}

	@Override
	public String getName(File file) {
		String name = file.getPath().substring(basePathLength);
		return name.replace('\\', '/');
	}
	
	
	
}
