package org.archboy.clobaframe.web.page.revision.local.impl;

import java.io.File;
import org.archboy.clobaframe.io.file.impl.DefaultFileBaseResourceInfo;
import org.archboy.clobaframe.web.page.revision.local.LocalRevisionPageResourceInfo;

public class DefaultLocalRevisionPageResourceInfo extends DefaultFileBaseResourceInfo implements LocalRevisionPageResourceInfo {

	private String name;

	public DefaultLocalRevisionPageResourceInfo(
			File file, String mimeType, String name) {
		super(file, mimeType);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
