package org.archboy.clobaframe.web.theme.local;

import java.io.File;
import org.archboy.clobaframe.io.file.impl.DefaultFileBaseResourceInfo;
import org.archboy.clobaframe.web.theme.ThemeResourceInfo;
import org.archboy.clobaframe.webresource.local.LocalWebResourceInfo;

public class LocalThemeResourceInfo extends LocalWebResourceInfo implements ThemeResourceInfo {

	private boolean isTemplate;
	
	public LocalThemeResourceInfo(File file, String mimeType, String name, boolean isTemplate) {
		super(file, mimeType, name);
		this.isTemplate = isTemplate;
	}
	
	@Override
	public boolean isTemplate() {
		return isTemplate;
	}

}
