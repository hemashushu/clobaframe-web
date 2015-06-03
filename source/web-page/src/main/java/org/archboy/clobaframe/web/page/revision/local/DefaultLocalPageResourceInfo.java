package org.archboy.clobaframe.web.page.revision.local;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.archboy.clobaframe.io.file.impl.DefaultFileBaseResourceInfo;
import org.archboy.clobaframe.webresource.WebResourceInfo;

public class DefaultLocalPageResourceInfo extends DefaultFileBaseResourceInfo implements LocalPageResourceInfo {

	private String name;

	public DefaultLocalPageResourceInfo(
			File file, String mimeType, String name) {
		super(file, mimeType);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
