package org.archboy.clobaframe.web.page.revision.local;

import org.archboy.clobaframe.web.page.revision.RevisionPageInfo;

/**
 *
 * @author yang
 */
public class LocalRevisionPageInfo extends RevisionPageInfo {
	
	private boolean readonly;

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

}
