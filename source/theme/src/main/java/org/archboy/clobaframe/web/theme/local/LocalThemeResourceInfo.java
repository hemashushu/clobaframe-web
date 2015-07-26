package org.archboy.clobaframe.web.theme.local;

import java.io.File;
import org.archboy.clobaframe.resource.local.LocalResourceInfo;
import org.archboy.clobaframe.web.theme.ThemeResourceInfo;

public class LocalThemeResourceInfo extends LocalResourceInfo implements ThemeResourceInfo {

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
