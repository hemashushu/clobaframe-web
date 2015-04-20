package org.archboy.clobaframe.web.doc.impl;

import java.util.Collection;
import java.util.Locale;
import javax.inject.Named;
import org.archboy.clobaframe.web.doc.Doc;
import org.archboy.clobaframe.web.doc.DocManager;

/**
 *
 * @author yang
 */
@Named
public class DocManagerImpl implements DocManager {

	@Override
	public Doc get(String name, Locale locale) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Collection<Locale> listLocale(String name) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Doc add(String name, String parentName, Locale locale, String title, String content, String authorName, String authorId) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void delete(String name, Locale locale) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
