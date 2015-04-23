package org.archboy.clobaframe.web.doc.revision.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.query.simplequery.SimpleQuery;
import org.archboy.clobaframe.web.doc.Doc;
import org.archboy.clobaframe.web.doc.DocProvider;
import org.archboy.clobaframe.web.doc.DocRepository;
import org.archboy.clobaframe.web.doc.revision.RevisionDoc;
import org.archboy.clobaframe.web.doc.revision.RevisionDocManager;

/**
 *
 * @author yang
 */
@Named
public class RevisionDocManagerImpl implements RevisionDocManager {

	@Inject
	private List<DocProvider> docProviders;
	
	@Inject
	private DocRepository docRepository;
	
	private Map<String, Map<Locale, Set<RevisionDoc>>> docMap = 
			new HashMap<String, Map<Locale, Set<RevisionDoc>>>();
	
	@PostConstruct
	public void init(){
		
		// sort the doc providers.
		docProviders.sort(new Comparator<DocProvider>() {

			@Override
			public int compare(DocProvider o1, DocProvider o2) {
				return o1.getPriority() - o2.getPriority();
			}
		});
		
		// get all docs and build doc map
		for (int idx=docProviders.size() - 1; idx>=0; idx--){
			DocProvider docProvider = docProviders.get(idx);
			
			Collection<Doc> docs = docProvider.getAll();
			
			for(Doc doc : docs){
				if (!(doc instanceof RevisionDoc)) {
					continue; // skip not revision doc
				}
				
				RevisionDoc rdoc = (RevisionDoc)doc;
				
				// get locale doc
				Map<Locale, Set<RevisionDoc>> localeDocs = docMap.get(rdoc.getName());
				if (localeDocs == null){
					localeDocs = new HashMap<Locale, Set<RevisionDoc>>();
					docMap.put(rdoc.getName(), localeDocs);
				}
				
				// get revisions
				Set<RevisionDoc> revisionDocs = localeDocs.get(rdoc.getLocale());
				if (revisionDocs == null) {
					revisionDocs = new HashSet<RevisionDoc>();
					localeDocs.put(rdoc.getLocale(), revisionDocs);
				}
				
				// add or replace the doc
				revisionDocs.add(rdoc);
			}
		}
	}
	
	@Override
	public Collection<Integer> listRevision(String name, Locale locale) {
		Map<Locale, Set<RevisionDoc>> localeDocs = docMap.get(name);
		if (localeDocs == null) {
			return null;
		}
		
		Set<RevisionDoc> revisionDocs = localeDocs.get(locale);
		if (revisionDocs == null){
			return null;
		}
		
			List<Integer> revisions = new ArrayList<Integer>();
			for (RevisionDoc doc : revisionDocs){
				revisions.add(doc.getRevision());
			}

			return revisions;
		}
		
	}

	@Override
	public int getActiveRevision(String name, Locale locale) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public RevisionDoc get(String name, Locale locale, int revision) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setActiveRevision(String name, Locale locale, int revision) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void delete(String name, Locale locale, int revision) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

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
