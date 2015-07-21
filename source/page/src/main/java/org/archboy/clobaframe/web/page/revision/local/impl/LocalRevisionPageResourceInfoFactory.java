package org.archboy.clobaframe.web.page.revision.local.impl;

import org.archboy.clobaframe.web.page.revision.local.impl.DefaultLocalRevisionPageResourceInfo;
import java.io.File;
import org.archboy.clobaframe.io.MimeTypeDetector;
import org.archboy.clobaframe.io.ResourceInfo;
import org.archboy.clobaframe.io.file.FileBaseResourceInfo;
import org.archboy.clobaframe.io.file.FileBaseResourceInfoFactory;
import org.archboy.clobaframe.io.file.impl.DefaultFileBaseResourceInfoFactory;
import org.archboy.clobaframe.web.page.revision.local.LocalRevisionPageResourceNameStrategy;

/**
 *
 * @author yang
 */
public class LocalRevisionPageResourceInfoFactory extends DefaultFileBaseResourceInfoFactory implements FileBaseResourceInfoFactory {

	private LocalRevisionPageResourceNameStrategy resourceNameStrategy;

	public LocalRevisionPageResourceInfoFactory(MimeTypeDetector mimeTypeDetector, LocalRevisionPageResourceNameStrategy resourceNameStrategy) {
		super(mimeTypeDetector);
		this.resourceNameStrategy = resourceNameStrategy;
	}
	
	@Override
	public FileBaseResourceInfo make(File file) {
		String name = resourceNameStrategy.getName(file);
		String mimeType = getMimeType(file);
		
		return new DefaultLocalRevisionPageResourceInfo(file, mimeType, name);
	}
}
