package org.archboy.clobaframe.web.page.revision.impl;

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
import org.archboy.clobaframe.web.page.Page;
import org.archboy.clobaframe.web.page.PageProvider;
import org.archboy.clobaframe.web.page.PageRepository;
import org.archboy.clobaframe.web.page.revision.RevisionPage;
import org.archboy.clobaframe.web.page.revision.RevisionPageManager;
import org.archboy.clobaframe.web.page.revision.RevisionPageRepository;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author yang
 */
@Named
public class RevisionPageManagerImpl implements RevisionPageManager {

	@Inject
	private List<PageProvider> pageProviders;
	
	@Inject
	private RevisionPageRepository revisionPageRepository;
	
	private static final String DEFAULT_LOCALE_NAME = "en";
	
	@Value("${clobaframe.web.page.defaultLocale}")
	private String localeName = DEFAULT_LOCALE_NAME;
	
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
		
		defaultLocale = Locale.forLanguageTag(localeName);
		
		// sort the page providers by the priority.
		// the highest priorty (that with less number) will put to the head.
		pageProviders.sort(new Comparator<PageProvider>() {

			@Override
			public int compare(PageProvider o1, PageProvider o2) {
				return o1.getPriority() - o2.getPriority();
			}
		});
		
		// get all pages and build page map and url name map.
		// try to get from the lower priorty proiver first.
		for (int idx=pageProviders.size() - 1; idx>=0; idx--){
			PageProvider pageProvider = pageProviders.get(idx);
			
			Collection<Page> pages = pageProvider.getAll();
			
			for(Page page : pages){
				if (!(page instanceof RevisionPage)) {
					continue; // skip none revision page
				}
				
				RevisionPage revisionPage = (RevisionPage)page;
				
				// get locale doc
				Map<Locale, Set<RevisionPage>> localePages = pageMap.get(revisionPage.getName());
				if (localePages == null){
					localePages = new HashMap<Locale, Set<RevisionPage>>();
					pageMap.put(revisionPage.getName(), localePages);
				}
				
				// get revisions
				Set<RevisionPage> revisionPages = localePages.get(revisionPage.getLocale());
				if (revisionPages == null) {
					revisionPages = new HashSet<RevisionPage>();
					localePages.put(revisionPage.getLocale(), revisionPages);
				}
				
				// add (or replace) the page.
				revisionPages.add(revisionPage);
				
				if (StringUtils.isNotEmpty(revisionPage.getUrlName())){
					urlMap.put(revisionPage.getUrlName(), revisionPage.getName());
				}
			}
		}
	}
	
	@Override
	public Collection<RevisionPage> listRevision(String name, Locale locale) {
		Map<Locale, Set<RevisionPage>> localePages = pageMap.get(name);
		if (localePages == null) {
			return null;
		}
		
		return localePages.get(locale);
	}

	@Override
	public int getActiveRevision(String name, Locale locale) {
		return revisionPageRepository.getActiveRevision(name, locale);
	}

	@Override
	public RevisionPage get(String name, Locale locale, int revision) {
		Collection<RevisionPage> revisionPages = listRevision(name, locale);
		if (revisionPages != null) {
			for (RevisionPage revisionPage : revisionPages) {
				if (revisionPage.getRevision() == revision) {
					return revisionPage;
				}
			}
		}
		return null;
	}

	@Override
	public void setActiveRevision(String name, Locale locale, int revision) {
		revisionPageRepository.setActiveRevision(name, locale, revision);
	}

	@Override
	public void delete(String name, Locale locale, int revision) {
		revisionPageRepository.delete(name, locale, revision);
	}

	@Override
	public Page get(String name, Locale locale) {
		return get(name, locale, 0);
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
	public Page update(String name, Locale locale, 
			String title, String content, 
			String urlName, String templateName, 
			String authorName, String authorId, String updateNote) {
		
		return revisionPageRepository.update(name, locale, 
				title, content, 
				urlName, templateName, 
				authorName, authorId, updateNote);
	}

	@Override
	public void delete(String name, Locale locale) {
		Collection<RevisionPage> revisionPages = listRevision(name, locale);
		if (revisionPages != null) {
			for (RevisionPage revisionPage : revisionPages) {
				revisionPageRepository.delete(name, locale, revisionPage.getRevision());
			}
		}
	}

	@Override
	public Locale getDefaultLocale() {
		return defaultLocale;
	}
}
