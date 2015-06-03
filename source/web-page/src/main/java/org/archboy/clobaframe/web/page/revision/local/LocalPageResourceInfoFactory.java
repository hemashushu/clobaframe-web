package org.archboy.clobaframe.web.page.revision.local;

import java.io.File;
import org.archboy.clobaframe.io.MimeTypeDetector;
import org.archboy.clobaframe.io.ResourceInfo;
import org.archboy.clobaframe.io.file.FileBaseResourceInfo;
import org.archboy.clobaframe.io.file.FileBaseResourceInfoFactory;

/**
 *
 * @author yang
 */
public class LocalPageResourceInfoFactory implements FileBaseResourceInfoFactory {

	private MimeTypeDetector mimeTypeDetector;
	private LocalPageResourceNameStrategy resourceNameStrategy;

	public LocalPageResourceInfoFactory(MimeTypeDetector mimeTypeDetector, LocalPageResourceNameStrategy resourceNameStrategy) {
		this.mimeTypeDetector = mimeTypeDetector;
		this.resourceNameStrategy = resourceNameStrategy;
	}
	
	@Override
	public FileBaseResourceInfo make(File file) {
		String name = resourceNameStrategy.getName(file);
		String mimeType = getMimeType(file);
		
		DefaultLocalPageResourceInfo webResourceInfo = new DefaultLocalPageResourceInfo(
				file, mimeType, name);
		return webResourceInfo;
	}
	
	private String getMimeType(File file){
		String fileName = file.getName();
		return mimeTypeDetector.getByExtensionName(fileName);
	}
	
}
