package org.archboy.clobaframe.web.page.revision.local;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.web.page.PageInfo;
import org.archboy.clobaframe.web.page.PageKey;
import org.archboy.clobaframe.web.page.PageProvider;
import org.archboy.clobaframe.web.page.revision.AbstractRevisionPageCollection;
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
public class LocalRevisionPageManagerImpl extends AbstractRevisionPageCollection implements RevisionPageManager {

	@Inject
	private List<RevisionPageProvider> revisionPageProviders;
	
	@Autowired(required = false)
	private RevisionPageRepository revisionPageRepository;
	
	private static final String DEFAULT_LOCALE = "en";
	
	@Value("${clobaframe.web.page.defaultLocale:" + DEFAULT_LOCALE + "}")
	private Locale defaultLocale;
	
	@PostConstruct
	public void init(){
		// get all pages and build page map and url name map.
		// try to get from the lower priorty proiver first.
		for (int idx=revisionPageProviders.size() - 1; idx>=0; idx--){
			PageProvider pageProvider = revisionPageProviders.get(idx);
			
			Collection<PageInfo> pages = pageProvider.getAll();
			
			for(PageInfo page : pages){
				if (page instanceof RevisionPageInfo) {
					// skip none revision page
					save((RevisionPageInfo)page);
				}
			}
		}
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
		super.save(revisionPage);
		return revisionPage;
	}

	@Override
	public void delete(PageKey pageKey, int revision) {
		revisionPageRepository.delete(pageKey, revision);
		super.delete(pageKey, revision);
	}
	
	@Override
	public void delete(PageKey pageKey) {
		revisionPageRepository.delete(pageKey);
		super.delete(pageKey);
	}

	@Override
	public void delete(String name) {
		revisionPageRepository.delete(name);
		super.delete(name);
	}
	
	@Override
	public RevisionPageInfo rollbackRevision(PageKey pageKey, int revision) {
		int v = getCurrentRevision(pageKey);
		if (v == revision) {
			throw new IllegalArgumentException("This is already the current revision");
		}
		
		v++;
			
		RevisionPageInfo old = get(pageKey, revision);

		RevisionPageInfo page = new RevisionPageInfo();
		page.setAuthorId(old.getAuthorId());
		page.setAuthorName(old.getAuthorName());
		page.setComment(old.getComment());
		page.setContent(old.getContent());
		page.setLastModified(old.getLastModified());
		page.setPageKey(old.getPageKey());
		page.setRevision(v);
		page.setTemplateName(old.getTemplateName());
		page.setTitle(old.getTitle());
		page.setUrlName(old.getUrlName());
			
		revisionPageRepository.delete(pageKey, revision);
		revisionPageRepository.save(pageKey, revision, 
				page.getTitle(), page.getContent(),
				page.getUrlName(), page.getTemplateName(),
				page.getAuthorName(), page.getAuthorId(),
				page.getComment());
				
		super.delete(pageKey, revision);
		super.save(page);
		return page;
	}

	
	
	@Override
	public Locale getDefaultLocale() {
		return defaultLocale;
	}
	
}
