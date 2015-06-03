package org.archboy.clobaframe.web.page.revision.local;

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
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.query.simplequery.SimpleQuery;
import org.archboy.clobaframe.web.page.Page;
import org.archboy.clobaframe.web.page.PageKey;
import org.archboy.clobaframe.web.page.PageProvider;
import org.archboy.clobaframe.web.page.PageRepository;
import org.archboy.clobaframe.web.page.revision.RevisionPage;
import org.archboy.clobaframe.web.page.revision.RevisionPageManager;
import org.archboy.clobaframe.web.page.revision.RevisionPageProvider;
import org.archboy.clobaframe.web.page.revision.RevisionPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author yang
 */
@Named
public class LocalRevisionPageManagerImpl implements RevisionPageManager {

	@Inject
	private List<RevisionPageProvider> revisionPageProviders;
	
	@Autowired(required = false)
	private RevisionPageRepository revisionPageRepository;
	
	private static final String DEFAULT_LOCALE = "en";
	
	@Value("${clobaframe.web.page.defaultLocale:" + DEFAULT_LOCALE + "}")
	private Locale defaultLocale;
	
	/**
	 * The map key is: "page name, locale, revision".
	 */
	private Map<String, Map<Locale, Set<RevisionPage>>> pageMap = 
			new HashMap<String, Map<Locale, Set<RevisionPage>>>();
	
	/**
	 * The URL name - page name map.
	 */
	private Map<String, String> urlMap = new HashMap<String, String>();
	
	@PostConstruct
	public void init(){
		// get all pages and build page map and url name map.
		// try to get from the lower priorty proiver first.
		for (int idx=revisionPageProviders.size() - 1; idx>=0; idx--){
			PageProvider pageProvider = revisionPageProviders.get(idx);
			
			Collection<Page> pages = pageProvider.getAll();
			
			for(Page page : pages){
				if (!(page instanceof RevisionPage)) {
					continue; // skip none revision page
				}
				
				RevisionPage revisionPage = (RevisionPage)page;
				
				// get locale doc
				Map<Locale, Set<RevisionPage>> localePages = pageMap.get(revisionPage.getPageKey().getName());
				if (localePages == null){
					localePages = new HashMap<Locale, Set<RevisionPage>>();
					pageMap.put(revisionPage.getPageKey().getName(), localePages);
				}
				
				// get revisions
				Set<RevisionPage> revisionPages = localePages.get(revisionPage.getPageKey().getLocale());
				if (revisionPages == null) {
					revisionPages = new HashSet<RevisionPage>();
					localePages.put(revisionPage.getPageKey().getLocale(), revisionPages);
				}
				
				// add (or replace) the page.
				revisionPages.add(revisionPage);
				
				if (StringUtils.isNotEmpty(revisionPage.getUrlName())){
					urlMap.put(revisionPage.getUrlName(), revisionPage.getPageKey().getName());
				}
			}
		}
	}
	
	@Override
	public Collection<RevisionPage> listRevision(PageKey pageKey) {
		Map<Locale, Set<RevisionPage>> localePages = pageMap.get(pageKey.getName());
		if (localePages == null) {
			return null;
		}
		
		return localePages.get(pageKey.getLocale());
	}

	@Override
	public int getCurrentRevision(PageKey pageKey) {
		Collection<RevisionPage> revisionPages = listRevision(pageKey);
		if (revisionPages == null) {
			return 0;
		}
		
		RevisionPage page = SimpleQuery.from(revisionPages).orderBy("revision").first();
		return page.getRevision();
	}

	@Override
	public RevisionPage get(PageKey pageKey, int revision) {
		Collection<RevisionPage> revisionPages = listRevision(pageKey);
		if (revisionPages == null) {
			return null;
		}
		
		return SimpleQuery.from(revisionPages).whereEquals("revision", revision).first();
	}

	@Override
	public void rollbackRevision(PageKey pageKey, int revision) {
		revisionPageRepository.rollbackRevision(pageKey, revision);
	}

	@Override
	public void delete(PageKey pageKey, int revision) {
		revisionPageRepository.delete(pageKey, revision);
	}

	@Override
	public Page get(PageKey pageKey) {
		Collection<RevisionPage> revisionPages = listRevision(pageKey);
		if (revisionPages == null) {
			return null;
		}
		
		return SimpleQuery.from(revisionPages).orderByDesc("revision").first();
	}

	@Override
	public String getName(String urlName) {
		return urlMap.get(urlName);
	}
	
	@Override
	public Collection<Locale> listLocale(String name) {
		Map<Locale, Set<RevisionPage>> localeDocs = pageMap.get(name);
		if (localeDocs == null) {
			return null;
		}
		
		return localeDocs.keySet();
	}

	@Override
	public Page update(PageKey pageKey,
		String title, String content, 
		String urlName, String templateName,
		String authorName, String authorId, String comment) {
		
		return revisionPageRepository.update(pageKey, title, content, 
				urlName, templateName, 
				authorName, authorId, comment);
	}

	@Override
	public void delete(PageKey pageKey) {
		revisionPageRepository.delete(pageKey);
	}

	@Override
	public Locale getDefaultLocale() {
		return defaultLocale;
	}
}
