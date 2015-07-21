package org.archboy.clobaframe.web.theme.local.impl;

import java.io.File;
import org.archboy.clobaframe.io.MimeTypeDetector;
import org.archboy.clobaframe.io.ResourceInfo;
import org.archboy.clobaframe.io.file.FileBaseResourceInfo;
import org.archboy.clobaframe.io.file.FileBaseResourceInfoFactory;
import org.archboy.clobaframe.io.file.impl.DefaultFileBaseResourceInfoFactory;
import org.archboy.clobaframe.web.theme.ThemeManager;
import org.archboy.clobaframe.web.theme.local.LocalThemeResourceInfo;
import org.archboy.clobaframe.webresource.local.LocalWebResourceNameStrategy;

/**
 *
 * @author yang
 */
public class LocalThemeResourceInfoFactory extends DefaultFileBaseResourceInfoFactory implements FileBaseResourceInfoFactory {

	private LocalWebResourceNameStrategy resourceNameStrategy;

	public LocalThemeResourceInfoFactory(MimeTypeDetector mimeTypeDetector, LocalWebResourceNameStrategy resourceNameStrategy) {
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
