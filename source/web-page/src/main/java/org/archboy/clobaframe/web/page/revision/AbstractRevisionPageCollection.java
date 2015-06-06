package org.archboy.clobaframe.web.page.revision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.query.simplequery.SimpleQuery;
import org.archboy.clobaframe.web.page.Page;
import org.archboy.clobaframe.web.page.PageKey;

/**
 *
 * @author yang
 */
public abstract class AbstractRevisionPageCollection {
	
	/**
	 * The map key is: "page name, locale, revision".
	 */
	protected Map<String, Map<Locale, Set<RevisionPage>>> pageMap = 
			new HashMap<String, Map<Locale, Set<RevisionPage>>>();
	
	/**
	 * The URL name - page name map.
	 */
	protected Map<String, String> urlMap = new HashMap<String, String>();
	
	public Collection<Locale> listLocale(String name) {
		Map<Locale, Set<RevisionPage>> localePages = pageMap.get(name);
		if (localePages == null) {
			return null;
		}
		
		return localePages.keySet();
	}
	
	public Collection<RevisionPage> listRevision(PageKey pageKey) {
		Map<Locale, Set<RevisionPage>> localePages = pageMap.get(pageKey.getName());
		if (localePages == null) {
			return null;
		}
		
		return localePages.get(pageKey.getLocale());
	}
	
	public Page get(PageKey pageKey) {
		Collection<RevisionPage> revisionPages = listRevision(pageKey);
		if (revisionPages == null) {
			return null;
		}
		
		return SimpleQuery.from(revisionPages).orderByDesc("revision").first();
	}
	
	public RevisionPage get(PageKey pageKey, int revision) {
		Collection<RevisionPage> revisionPages = listRevision(pageKey);
		if (revisionPages == null) {
			return null;
		}
		
		return SimpleQuery.from(revisionPages).whereEquals("revision", revision).first();
	}
	
	public int getCurrentRevision(PageKey pageKey) {
		Collection<RevisionPage> revisionPages = listRevision(pageKey);
		if (revisionPages == null) {
			throw new IllegalArgumentException("No this page key: " + pageKey);
		}
		
		RevisionPage page = SimpleQuery.from(revisionPages).orderByDesc("revision").first();
		return page.getRevision();
	}

	public String getByUrlName(String urlName) {
		return urlMap.get(urlName);
	}
	
	public void delete(String name){
		
		// get url names
		List<String> urlNames = new ArrayList<String>();
		
		Map<Locale, Set<RevisionPage>> localePages = pageMap.get(name);
		if (localePages == null) {
			return;
		}
		
		for(Set<RevisionPage> revisionPages : localePages.values()) {
			for (RevisionPage revisionPage : revisionPages) {
				String urlName = revisionPage.getUrlName();
				if (StringUtils.isNotEmpty(urlName)){
					urlNames.add(urlName);
				}
			}
		}
		
		// remove page by name
		pageMap.remove(name);
		
		// remove url names
		for (String urlName : urlNames) {
			urlMap.remove(urlName);
		}
	}
	
	public void delete(PageKey pageKey) {
		Map<Locale, Set<RevisionPage>> localePages = pageMap.get(pageKey.getName());
		if (localePages == null) {
			return;
		}
		
		Set<RevisionPage> revisionPages = localePages.get(pageKey.getLocale());
		
		if (revisionPages == null) {
			return;
		}
		
		// get url names
		List<String> urlNames = new ArrayList<String>();

		for (RevisionPage revisionPage : revisionPages) {
			String urlName = revisionPage.getUrlName();
			if (StringUtils.isNotEmpty(urlName)){
				urlNames.add(urlName);
			}
		}
		
		// remove page by locale
		localePages.remove(pageKey.getLocale());
		
		// remove url names
		for (String urlName : urlNames) {
			urlMap.remove(urlName);
		}
	}
	
	public void delete(PageKey pageKey, int revision) {
		Collection<RevisionPage> revisionPages = listRevision(pageKey);
		if (revisionPages == null) {
			return;
		}
		
		RevisionPage revisionPage = SimpleQuery.from(revisionPages).whereEquals("revision", revision).first();
		if (revisionPage == null) {
			return;
		}
		
		// remove page by revision
		revisionPages.remove(revisionPage);
		
		// remove url name
		String urlName = revisionPage.getUrlName();
		if (StringUtils.isNotEmpty(urlName)) {
			urlMap.remove(urlName);
		}
		
	}

	/**
	 * Add page to collection.
	 * 
	 * @param revisionPage 
	 */
	protected void save(RevisionPage revisionPage) {
		PageKey pageKey = revisionPage.getPageKey();
		
		// get locale doc
		Map<Locale, Set<RevisionPage>> localePages = pageMap.get(pageKey.getName());
		if (localePages == null){
			localePages = new HashMap<Locale, Set<RevisionPage>>();
			pageMap.put(pageKey.getName(), localePages);
		}

		// get revisions
		Set<RevisionPage> revisionPages = localePages.get(pageKey.getLocale());
		if (revisionPages == null) {
			revisionPages = new HashSet<RevisionPage>();
			localePages.put(pageKey.getLocale(), revisionPages);
		}

		// get the exist revision
//		RevisionPage exists = SimpleQuery.from(revisionPages).whereEquals("revision", revisionPage.getRevision()).first();
//		if (exists != null) {
//			
//			// remove the exist page.
//			revisionPages.remove(exists);
//			
//			// remove the exists custom page url.
//			if (StringUtils.isNotEmpty(exists.getUrlName())){
//				urlMap.remove(exists.getUrlName());
//			}
//		}
		
		// add the page.
		revisionPages.add(revisionPage);

		// add the custom page url.
		if (StringUtils.isNotEmpty(revisionPage.getUrlName())){
			urlMap.put(revisionPage.getUrlName(), revisionPage.getPageKey().getName());
		}
	}
}
