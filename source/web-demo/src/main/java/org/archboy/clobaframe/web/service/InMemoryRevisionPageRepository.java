package org.archboy.clobaframe.web.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.inject.Named;
import org.archboy.clobaframe.web.page.PageInfo;
import org.archboy.clobaframe.web.page.PageKey;
import static org.archboy.clobaframe.web.page.PageProvider.PRIORITY_NORMAL;
import org.archboy.clobaframe.web.page.revision.RevisionPageInfo;
import org.archboy.clobaframe.web.page.revision.RevisionPageProvider;
import org.archboy.clobaframe.web.page.revision.RevisionPageRepository;
import org.archboy.clobaframe.web.page.revision.impl.AbstractRevisionPageCollection;

/**
 *
 * @author yang
 */
@Named
public class InMemoryRevisionPageRepository extends AbstractRevisionPageCollection implements RevisionPageProvider, RevisionPageRepository {
	
	@Override
	public Collection<PageInfo> getAll() {
		List<PageInfo> pages = new ArrayList<PageInfo>();

		for(Map<Locale, Set<RevisionPageInfo>> localePages : pageMap.values()){
			for(Set<RevisionPageInfo> revisionPages : localePages.values()) {
				for (RevisionPageInfo revisionPage : revisionPages) {
					pages.add(revisionPage);
				}
			}
		}

		return pages;
	}

	@Override
	public int getOrder() {
		return PRIORITY_NORMAL;
	}

	@Override
	public PageInfo save(PageKey pageKey, int revision, String title, String content, String urlName, String templateName, String authorName, String authorId, String comment) {

		RevisionPageInfo page = new RevisionPageInfo();

		page.setAuthorId(authorId);
		page.setAuthorName(authorName);
		page.setComment(comment);
		page.setContent(content);
		page.setLastModified(new Date());
		page.setPageKey(pageKey);
		page.setRevision(revision);
		page.setTemplateName(templateName);
		page.setTitle(title);
		page.setUrlName(urlName);

		super.save(page);
		return page;
	}

	@Override
	public PageInfo save(PageKey pageKey, String title, String content, String urlName, String templateName, String authorName, String authorId, String comment) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
