package org.archboy.clobaframe.web.theme.local.impl;

import java.io.File;
import org.archboy.clobaframe.io.MimeTypeDetector;
import org.archboy.clobaframe.io.file.FileBaseResourceInfo;
import org.archboy.clobaframe.io.file.FileBaseResourceInfoFactory;
import org.archboy.clobaframe.io.file.impl.DefaultFileBaseResourceInfoFactory;
import org.archboy.clobaframe.resource.local.LocalResourceNameStrategy;
import org.archboy.clobaframe.web.theme.ThemeManager;
import org.archboy.clobaframe.web.theme.local.LocalThemeResourceInfo;

/**
 *
 * @author yang
 */
public class LocalThemeResourceInfoFactory extends DefaultFileBaseResourceInfoFactory implements FileBaseResourceInfoFactory {

	private LocalResourceNameStrategy resourceNameStrategy;

	public LocalThemeResourceInfoFactory(MimeTypeDetector mimeTypeDetector, LocalResourceNameStrategy resourceNameStrategy) {
		super(mimeTypeDetector);
		this.resourceNameStrategy = resourceNameStrategy;
	}
	
	@Override
	public FileBaseResourceInfo make(File file) {
		String name = resourceNameStrategy.getName(file);
		String mimeType = getMimeType(file);
		boolean isTempate = ThemeManager.MIME_TYPE_VELOCITY_TEMPLATE.equals(mimeType);
		
		return new LocalThemeResourceInfo(file, mimeType, name, isTempate);
	}
}
