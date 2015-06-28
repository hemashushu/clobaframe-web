package org.archboy.clobaframe.web.page.revision.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.query.simplequery.SimpleQuery;
import org.archboy.clobaframe.web.page.PageInfo;
import org.archboy.clobaframe.web.page.PageKey;
import org.archboy.clobaframe.web.page.PageProvider;
import org.archboy.clobaframe.web.page.revision.RevisionPageInfo;
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
public class RevisionPageManagerImpl implements RevisionPageManager {

	@Inject
	private List<RevisionPageProvider> revisionPageProviders;
	
	@Autowired(required = false)
	private RevisionPageRepository revisionPageRepository;
	
	private static final String DEFAULT_LOCALE = "en";
	
	@Value("${clobaframe.web.page.defaultLocale:" + DEFAULT_LOCALE + "}")
	private Locale defaultLocale;
	
	@Override
	public Collection<Locale> listLocale(String name) {
		List<Locale> locales = new ArrayList<Locale>();
		for (RevisionPageProvider pageProvider : revisionPageProviders){
			locales.addAll(pageProvider.listLocale(name));
		}
		return locales;
	}

	@Override
	public PageInfo get(PageKey pageKey) {
		for (RevisionPageProvider pageProvider : revisionPageProviders){
			PageInfo pageInfo = pageProvider.get(pageKey);
			if (pageInfo != null) {
				return pageInfo;
			}
		}
		
		return null;
	}

	@Override
	public String getByUrlName(String urlName) {
		for (RevisionPageProvider pageProvider : revisionPageProviders){
			String pageName = pageProvider.getByUrlName(urlName);
			if (pageName != null) {
				return pageName;
			}
		}
		
		return null;
	}

	@Override
	public Collection<PageInfo> list() {
		List<PageInfo> pageInfos = new ArrayList<PageInfo>();
		for (RevisionPageProvider pageProvider : revisionPageProviders){
			pageInfos.addAll(pageProvider.list());
		}
		return pageInfos;
	}
	
	@Override
	public Collection<PageInfo> listByLocale(Locale locale) {
		List<PageInfo> pageInfos = new ArrayList<PageInfo>();
		for (RevisionPageProvider pageProvider : revisionPageProviders){
			pageInfos.addAll(pageProvider.listByLocale(locale));
		}
		return pageInfos;
	}

	@Override
	public RevisionPageInfo get(PageKey pageKey, int revision) {
		for (RevisionPageProvider pageProvider : revisionPageProviders){
			RevisionPageInfo pageInfo = pageProvider.get(pageKey, revision);
			if (pageInfo != null) {
				return pageInfo;
			}
		}
		return null;
	}

	@Override
	public Collection<RevisionPageInfo> listRevision(PageKey pageKey) {
		List<RevisionPageInfo> pageInfos = new ArrayList<RevisionPageInfo>();
		for (RevisionPageProvider pageProvider : revisionPageProviders){
			pageInfos.addAll(pageProvider.listRevision(pageKey));
		}
		return pageInfos;
	}

	@Override
	public PageInfo save(PageKey pageKey,
		String title, String content, 
		String urlName, String templateName,
		String authorName, String authorId, String comment) {
		
		int revision = 0;
		RevisionPageInfo exist = (RevisionPageInfo)get(pageKey);
		if (exist != null) {
			revision = exist.getRevision() + 1;
		}
		
		RevisionPageInfo revisionPage = (RevisionPageInfo)revisionPageRepository.save(
				pageKey, revision, title, content, 
				urlName, templateName, 
				authorName, authorId, comment);
		//super.save(revisionPage);
		return revisionPage;
	}

	@Override
	public void delete(PageKey pageKey, int revision) {
		revisionPageRepository.delete(pageKey, revision);
		//super.delete(pageKey, revision);
	}
	
	@Override
	public void delete(PageKey pageKey) {
		revisionPageRepository.delete(pageKey);
		//super.delete(pageKey);
	}

	@Override
	public void delete(String name) {
		revisionPageRepository.delete(name);
		//super.delete(name);
	}
	
	protected int getLatestRevision(PageKey pageKey) {
		Collection<RevisionPageInfo> revisionPages = listRevision(pageKey);
		if (revisionPages == null) {
			throw new IllegalArgumentException("No this page key: " + pageKey);
		}
		
		RevisionPageInfo page = SimpleQuery.from(revisionPages).orderByDesc("revision").first();
		return page.getRevision();
	}
	
	@Override
	public RevisionPageInfo rollbackRevision(PageKey pageKey, int revision) {
		int v = getLatestRevision(pageKey);
		if (v == revision) {
			throw new IllegalArgumentException("This is already the current revision");
		}
		
		v++;
			
		RevisionPageInfo old = get(pageKey, revision);

//		RevisionPageInfo page = new RevisionPageInfo();
//		page.setAuthorId(old.getAuthorId());
//		page.setAuthorName(old.getAuthorName());
//		page.setComment(old.getComment());
//		page.setContent(old.getContent());
//		page.setLastModified(old.getLastModified());
//		page.setPageKey(old.getPageKey());
//		page.setRevision(v);
//		page.setTemplateName(old.getTemplateName());
//		page.setTitle(old.getTitle());
//		page.setUrlName(old.getUrlName());
			
		revisionPageRepository.delete(pageKey, revision);
		return (RevisionPageInfo)revisionPageRepository.save(pageKey, v, 
				old.getTitle(), old.getContent(),
				old.getUrlName(), old.getTemplateName(),
				old.getAuthorName(), old.getAuthorId(),
				old.getComment());
				
		//super.delete(pageKey, revision);
		//super.save(page);
		//return page;
	}
	
	@Override
	public Locale getDefaultLocale() {
		return defaultLocale;
	}
}
