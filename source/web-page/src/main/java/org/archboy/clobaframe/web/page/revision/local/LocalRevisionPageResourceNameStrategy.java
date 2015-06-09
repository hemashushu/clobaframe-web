package org.archboy.clobaframe.web.page.revision.local;

import java.io.File;
import org.archboy.clobaframe.io.file.local.LocalFileNameStrategy;

/**
 * Generate the resource name.
 * 
 * @author yang
 */
public interface LocalRevisionPageResourceNameStrategy extends LocalFileNameStrategy {
	
	String getName(File file);
	
}
