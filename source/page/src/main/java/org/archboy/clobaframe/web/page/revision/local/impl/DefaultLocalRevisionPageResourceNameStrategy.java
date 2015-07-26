package org.archboy.clobaframe.web.page.revision.local.impl;

import java.io.File;
import org.archboy.clobaframe.io.file.local.DefaultLocalFileNameStrategy;
import org.archboy.clobaframe.web.page.revision.local.LocalRevisionPageResourceNameStrategy;

/**
 * 
 * @author yang
 */
public class DefaultLocalRevisionPageResourceNameStrategy 
	extends DefaultLocalFileNameStrategy
	implements LocalRevisionPageResourceNameStrategy {
	
	private int basePathLengthPlusOne;

	public DefaultLocalRevisionPageResourceNameStrategy(File basePath) {
		super(basePath);
		// the base path length plus 1 to exclude the 
		// resource file name path '/' prefix character.
		this.basePathLengthPlusOne = basePath.getPath().length() + 1; 
	}

	@Override
	public String getName(File file) {
		String name = file.getPath().substring(basePathLengthPlusOne);
		return name.replace('\\', '/');
	}
}
