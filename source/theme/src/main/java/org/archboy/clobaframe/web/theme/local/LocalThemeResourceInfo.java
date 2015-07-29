package org.archboy.clobaframe.web.theme.local;

import java.io.File;
import org.archboy.clobaframe.resource.local.LocalResourceInfo;
import org.archboy.clobaframe.web.theme.ThemeResourceInfo;

public class LocalThemeResourceInfo extends LocalResourceInfo implements ThemeResourceInfo {

	//private boolean isTemplate;
	private int contentType;
	
	public LocalThemeResourceInfo(File file, String mimeType, String name, int contentType) { // boolean isTemplate) {
		super(file, mimeType, name);
		//this.isTemplate = isTemplate;
		this.contentType = contentType;
	}
	
//	@Override
//	public boolean isTemplate() {
//		return isTemplate;
//	}

	@Override
	public int getContentType() {
		return contentType;
	}
}
