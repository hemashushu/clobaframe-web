package org.archboy.clobaframe.web.page.revision;

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
import org.archboy.clobaframe.web.page.revision.impl.AbstractPreloadRevisionPageProvider;

/**
 *
 * @author yang
 */
@Named
public class InMemoryRevisionPageRepository extends AbstractPreloadRevisionPageProvider implements RevisionPageRepository {

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

		super.add(page);
		return page;
	}

	@Override
	public PageInfo save(PageKey pageKey, String title, String content, String urlName, String templateName, String authorName, String authorId, String comment) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void delete(PageKey pageKey, int revision) {
		super.remove(pageKey, revision);
	}

	@Override
	public void delete(String name) {
		super.remove(name);
	}

	@Override
	public void delete(PageKey pageKey) {
		super.remove(pageKey);
	}
}
