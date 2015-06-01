package org.archboy.clobaframe.web.controller.exception;

import java.io.FileNotFoundException;

/**
 *
 * @author yang
 */
public class PageNotFound extends FileNotFoundException{
	
	private static final long serialVersionUID = -1L;
	private String path;

	public PageNotFound(String path) {
		super();
		this.path = path;
	}
}
