package org.archboy.clobaframe.web.page.revision;

import org.archboy.clobaframe.web.page.revision.impl.AbstractPreloadRevisionPageProvider;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.query.simplequery.SimpleQuery;
import org.archboy.clobaframe.web.page.PageInfo;
import org.archboy.clobaframe.web.page.PageKey;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author yang
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"/applicationContext.xml"})
@WebAppConfiguration("src/test/resources/webapp")
@ContextConfiguration(locations = { "/webapp/WEB-INF/servlet.xml"})
public class RevisionPageManagerTest {

	@Inject
	private RevisionPageManager revisionPageManager;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testList() {

		PageKey about = new PageKey("about", Locale.ENGLISH);
		PageKey about_zh_CN = new PageKey("about", Locale.SIMPLIFIED_CHINESE);
		PageKey about_ja = new PageKey("about", Locale.JAPANESE);
		PageKey contact = new PageKey("contact", Locale.ENGLISH);
		PageKey developers = new PageKey("developers", Locale.ENGLISH);
		PageKey privacy = new PageKey("privacy", Locale.ENGLISH);
		PageKey terms_zh_CN = new PageKey("terms", Locale.SIMPLIFIED_CHINESE);
		PageKey terms_ja = new PageKey("terms", Locale.JAPANESE);
		PageKey api = new PageKey("developers/api", Locale.ENGLISH);
		PageKey user = new PageKey("developers/api/user", Locale.ENGLISH);

		// test default locale
		assertEquals(Locale.ENGLISH, revisionPageManager.getDefaultLocale());

		// test list all
		Collection<PageInfo> pageInfos1 = revisionPageManager.list();
		Collection<PageKey> keys1 = Arrays.asList(about, about_zh_CN, about_ja, contact, developers, privacy, terms_zh_CN, terms_ja, api, user);
		for (PageInfo pageInfo : pageInfos1) {
			assertTrue(keys1.contains(pageInfo.getPageKey()));
		}

		// test list all by zh_CN
		Collection<PageInfo> pageInfos2 = revisionPageManager.listByLocale(Locale.SIMPLIFIED_CHINESE);
		Collection<PageKey> keys2 = Arrays.asList(about_zh_CN, terms_zh_CN);
		for (PageInfo pageInfo : pageInfos2) {
			assertTrue(keys2.contains(pageInfo.getPageKey()));
		}

		// test list all by jp
		Collection<PageInfo> pageInfos3 = revisionPageManager.listByLocale(Locale.JAPANESE);
		Collection<PageKey> keys3 = Arrays.asList(about_ja, terms_ja);
		for (PageInfo pageInfo : pageInfos3) {
			assertTrue(keys3.contains(pageInfo.getPageKey()));
		}

		// test list all by en
		Collection<PageInfo> pageInfos4 = revisionPageManager.listByLocale(Locale.ENGLISH);
		Collection<PageKey> keys4 = Arrays.asList(about, contact, developers, privacy, api, user);
		for (PageInfo pageInfo : pageInfos4) {
			assertTrue(keys4.contains(pageInfo.getPageKey()));
		}

		// test list locale
		Collection<Locale> locales1 = revisionPageManager.listLocale("about");
		assertEquals(3, locales1.size());
		assertTrue(locales1.contains(Locale.ENGLISH));
		assertTrue(locales1.contains(Locale.JAPANESE));
		assertTrue(locales1.contains(Locale.SIMPLIFIED_CHINESE));

		// test list locale - no locale
		Collection<Locale> locales2 = revisionPageManager.listLocale("contact");
		assertEquals(1, locales2.size());
		assertTrue(locales2.contains(Locale.ENGLISH));

		// test list locale - no default locale
		Collection<Locale> locales3 = revisionPageManager.listLocale("terms");
		assertEquals(2, locales3.size());
		assertTrue(locales3.contains(Locale.JAPANESE));
		assertTrue(locales3.contains(Locale.SIMPLIFIED_CHINESE));

		// test list by none-exist page name
		assertTrue(revisionPageManager.listLocale("none-exist").isEmpty());

		// test list revision
		Collection<RevisionPageInfo> revisions1 = revisionPageManager.listRevision(about);
		assertEquals(3, revisions1.size());
		assertNotNull(SimpleQuery.from(revisions1).whereEquals("revision", 0).first());
		assertNotNull(SimpleQuery.from(revisions1).whereEquals("revision", 1).first());
		assertNotNull(SimpleQuery.from(revisions1).whereEquals("revision", 2).first());

		// test list revision - not continuous
		Collection<RevisionPageInfo> revisions2 = revisionPageManager.listRevision(about_zh_CN);
		assertEquals(2, revisions2.size());
		assertNotNull(SimpleQuery.from(revisions2).whereEquals("revision", 0).first());
		assertNotNull(SimpleQuery.from(revisions2).whereEquals("revision", 8).first());

		// test list revision - no revision
		Collection<RevisionPageInfo> revisions3 = revisionPageManager.listRevision(new PageKey("about", Locale.JAPANESE));
		assertEquals(1, revisions3.size());
		assertNotNull(SimpleQuery.from(revisions3).whereEquals("revision", 0).first());

		// test list none-exist page
		assertTrue(revisionPageManager.listRevision(new PageKey("none-exist", Locale.ENGLISH)).isEmpty());

		// test get a page - by the current revison - latest revision
		RevisionPageInfo page1 = (RevisionPageInfo) revisionPageManager.get(about);
		assertEquals(2, page1.getRevision());
		assertEquals("about r2", page1.getTitle());
		assertEquals("this is about r2", page1.getContent());
		//assertEquals(2, revisionPageManager.getCurrentRevision(new PageKey("about", Locale.ENGLISH)));

		// test get a page - by the current revison - latest revision - different locale
		RevisionPageInfo page2 = (RevisionPageInfo) revisionPageManager.get(about_zh_CN);
		assertEquals(8, page2.getRevision());
		assertEquals("about zh_CN r8", page2.getTitle());
		assertEquals("this about zh_CN r8", page2.getContent());

		//assertEquals(8, revisionPageManager.getCurrentRevision(new PageKey("about", Locale.SIMPLIFIED_CHINESE)));
		// test get a page - by the current revison - no revision
		RevisionPageInfo page3 = (RevisionPageInfo) revisionPageManager.get(new PageKey("about", Locale.JAPANESE));
		assertEquals(0, page3.getRevision());
		assertEquals("about ja", page3.getTitle());
		//assertEquals(0, revisionPageManager.getCurrentRevision(new PageKey("about", Locale.JAPANESE)));

		// test get get a page with path name
		RevisionPageInfo page4 = (RevisionPageInfo) revisionPageManager.get(api);
		assertEquals(0, page4.getRevision());
		assertEquals("api", page4.getTitle());
		assertEquals("this is api", page4.getContent());
		//assertEquals(0, revisionPageManager.getCurrentRevision(new PageKey("developers/api", Locale.ENGLISH)));

		// test get none exist local
		assertNull(revisionPageManager.get(new PageKey("about", Locale.FRENCH)));

		// test get a specify revision
		RevisionPageInfo revisionPage1 = (RevisionPageInfo) revisionPageManager.get(about, 1);
		assertEquals(1, revisionPage1.getRevision());
		assertEquals("about r1", revisionPage1.getTitle());

		// test get a specify revision - no revision
		RevisionPageInfo revisionPage2 = (RevisionPageInfo) revisionPageManager.get(new PageKey("about", Locale.JAPANESE), 0);
		assertEquals(0, revisionPage2.getRevision());
		assertEquals("about ja", revisionPage2.getTitle());

		// test get none-exist revision
		assertNull(revisionPageManager.get(new PageKey("about", Locale.ENGLISH), 10));
	}

	@Test
	public void testGet() {
		// check template name
		RevisionPageInfo page1 = (RevisionPageInfo) revisionPageManager.get(new PageKey("contact", Locale.ENGLISH));
		assertEquals("mobile-page", page1.getTemplateName());

		// check url name
		RevisionPageInfo page2 = (RevisionPageInfo) revisionPageManager.get(new PageKey("privacy", Locale.ENGLISH));
		assertEquals("help/privacy", page2.getUrlName());
		assertEquals(page2.getPageKey().getName(), revisionPageManager.getByUrlName("help/privacy"));

		// check both template name and url name
		RevisionPageInfo page3 = (RevisionPageInfo) revisionPageManager.get(new PageKey("terms", Locale.SIMPLIFIED_CHINESE));
		assertEquals("help/terms", page3.getUrlName());
		assertEquals("fullscreen/responsive", page3.getTemplateName());
		assertEquals(page3.getPageKey().getName(), revisionPageManager.getByUrlName("help/terms"));

		// check the difference revision template name and url name.
//		RevisionPageInfo page4 = (RevisionPageInfo)revisionPageManager.get(new PageKey("terms", Locale.SIMPLIFIED_CHINESE), 23);
//		assertEquals("help/terms", page4.getUrlName());
//		assertEquals("fullscreen/responsive", page4.getTemplateName());
//		assertEquals(page4.getPageKey().getName(), revisionPageManager.getByUrlName("help/terms"));
		// test get by url name - none exist
		assertNull(revisionPageManager.getByUrlName("none-exist"));
	}

	@Test
	public void testSave() {
		// create new page
		RevisionPageInfo page1 = (RevisionPageInfo) revisionPageManager.save(
				new PageKey("share", Locale.ENGLISH),
				"share", "share this",
				null, null, null, null, null);

		assertEquals(new PageKey("share", Locale.ENGLISH), page1.getPageKey());
		assertEquals("share", page1.getTitle());
		assertEquals("share this", page1.getContent());
		assertEquals(0, page1.getRevision());

		assertNull(page1.getTemplateName());
		assertNull(page1.getUrlName());
		assertNull(page1.getAuthorId());
		assertNull(page1.getAuthorName());
		assertNull(page1.getComment());
		assertNotNull(page1.getLastModified());

		// check
		RevisionPageInfo pageByGet1 = (RevisionPageInfo) revisionPageManager.get(new PageKey("share", Locale.ENGLISH));
		assertEquals(page1, pageByGet1);

		// create new revision
		RevisionPageInfo page2 = (RevisionPageInfo) revisionPageManager.save(
				new PageKey("share", Locale.ENGLISH),
				"share r2", "share this r2",
				"share", "fullscreen", "authorName", "authorId", "second commit");

		assertEquals(new PageKey("share", Locale.ENGLISH), page2.getPageKey());
		assertEquals("share r2", page2.getTitle());
		assertEquals("share this r2", page2.getContent());
		assertEquals(1, page2.getRevision());

		assertEquals("fullscreen", page2.getTemplateName());
		assertEquals("share", page2.getUrlName());
		assertEquals("authorId", page2.getAuthorId());
		assertEquals("authorName", page2.getAuthorName());
		assertEquals("second commit", page2.getComment());
		assertNotNull(page2.getLastModified());

		// check
		RevisionPageInfo pageByGet2 = (RevisionPageInfo) revisionPageManager.get(new PageKey("share", Locale.ENGLISH));
		assertEquals(page2, pageByGet2);

		// check locale
		Collection<Locale> locales1 = revisionPageManager.listLocale("share");
		assertEquals(1, locales1.size());
		assertTrue(locales1.contains(Locale.ENGLISH));

		// check revision
		Collection<RevisionPageInfo> revisions1 = revisionPageManager.listRevision(new PageKey("share", Locale.ENGLISH));
		assertEquals(2, revisions1.size());
		assertNotNull(SimpleQuery.from(revisions1).whereEquals("revision", 0).first());
		assertNotNull(SimpleQuery.from(revisions1).whereEquals("revision", 1).first());

		// create another locale
		RevisionPageInfo pageLocale1 = (RevisionPageInfo) revisionPageManager.save(
				new PageKey("share", Locale.SIMPLIFIED_CHINESE),
				"gong xiang", "gong xiang zhe ge ye mian",
				null, null, null, null, null);

		assertEquals(new PageKey("share", Locale.SIMPLIFIED_CHINESE), pageLocale1.getPageKey());
		assertEquals("gong xiang", pageLocale1.getTitle());
		assertEquals("gong xiang zhe ge ye mian", pageLocale1.getContent());
		assertEquals(0, pageLocale1.getRevision());

		// check
		RevisionPageInfo pageByGetLocale1 = (RevisionPageInfo) revisionPageManager.get(new PageKey("share", Locale.SIMPLIFIED_CHINESE));
		assertEquals(pageLocale1, pageByGetLocale1);

		// check locale
		Collection<Locale> locales2 = revisionPageManager.listLocale("share");
		assertEquals(2, locales2.size());
		assertTrue(locales2.contains(Locale.ENGLISH));
		assertTrue(locales2.contains(Locale.SIMPLIFIED_CHINESE));

		// create another revision and prepare to test rollback
		RevisionPageInfo page3 = (RevisionPageInfo) revisionPageManager.save(
				new PageKey("share", Locale.ENGLISH),
				"share r3", "share this r3",
				null, null, null, null, null);

		assertEquals(2, ((RevisionPageInfo) revisionPageManager.get(new PageKey("share", Locale.ENGLISH))).getRevision());

		RevisionPageInfo pageByRollback1 = (RevisionPageInfo) revisionPageManager.rollbackRevision(
				new PageKey("share", Locale.ENGLISH), 1);

		assertEquals("share r2", pageByRollback1.getTitle());
		assertEquals("share this r2", pageByRollback1.getContent());
		assertEquals(3, pageByRollback1.getRevision());

		// check
		RevisionPageInfo page4 = (RevisionPageInfo) revisionPageManager.get(new PageKey("share", Locale.ENGLISH));
		assertEquals(pageByRollback1, page4);

		// check the old revision
		assertNull(revisionPageManager.get(new PageKey("share", Locale.ENGLISH), 1));

		// rollback again
		RevisionPageInfo pageByRollback2 = (RevisionPageInfo) revisionPageManager.rollbackRevision(
				new PageKey("share", Locale.ENGLISH), 2);

		assertEquals("share r3", pageByRollback2.getTitle());
		assertEquals("share this r3", pageByRollback2.getContent());
		assertEquals(4, pageByRollback2.getRevision());

		RevisionPageInfo page5 = (RevisionPageInfo) revisionPageManager.get(new PageKey("share", Locale.ENGLISH));
		assertEquals(pageByRollback2, page5);

		// check the revisions
		Collection<RevisionPageInfo> revisions2 = revisionPageManager.listRevision(new PageKey("share", Locale.ENGLISH));
		assertEquals(3, revisions2.size());
		assertNotNull(SimpleQuery.from(revisions2).whereEquals("revision", 0).first());
		assertNotNull(SimpleQuery.from(revisions2).whereEquals("revision", 3).first());
		assertNotNull(SimpleQuery.from(revisions2).whereEquals("revision", 4).first());

		// test delete an old revision
		revisionPageManager.delete(new PageKey("share", Locale.ENGLISH), 0);
		assertNull(revisionPageManager.get(new PageKey("share", Locale.ENGLISH), 0));

		// check the revisions
		Collection<RevisionPageInfo> revisions3 = revisionPageManager.listRevision(new PageKey("share", Locale.ENGLISH));
		assertEquals(2, revisions3.size());
		assertNotNull(SimpleQuery.from(revisions3).whereEquals("revision", 3).first());
		assertNotNull(SimpleQuery.from(revisions3).whereEquals("revision", 4).first());

		// test delete the current revision
		revisionPageManager.delete(new PageKey("share", Locale.ENGLISH), 4);
		assertNull(revisionPageManager.get(new PageKey("share", Locale.ENGLISH), 4));

		assertEquals(3, ((RevisionPageInfo) revisionPageManager.get(new PageKey("share", Locale.ENGLISH))).getRevision());

		// check the revisions
		Collection<RevisionPageInfo> revisions4 = revisionPageManager.listRevision(new PageKey("share", Locale.ENGLISH));
		assertEquals(1, revisions4.size());
		assertNotNull(SimpleQuery.from(revisions4).whereEquals("revision", 3).first());

		// test delete by key
		revisionPageManager.delete(new PageKey("share", Locale.ENGLISH));

		assertNull(revisionPageManager.get(new PageKey("share", Locale.ENGLISH)));

		// check locale
		Collection<Locale> locales3 = revisionPageManager.listLocale("share");
		assertEquals(1, locales3.size());
		assertTrue(locales3.contains(Locale.SIMPLIFIED_CHINESE));

		// test delete by name
		revisionPageManager.delete("share");
		assertTrue(revisionPageManager.listLocale("share").isEmpty());
	}

	@Named
	public static class TestingRevisionPageProviderAndRepository extends AbstractPreloadRevisionPageProvider implements RevisionPageRepository {

//		@Override
//		public Collection<PageInfo> list() {
//			List<PageInfo> pages = new ArrayList<PageInfo>();
//			
//			for(Map<Locale, Set<RevisionPageInfo>> localePages : pageMap.values()){
//				for(Set<RevisionPageInfo> revisionPages : localePages.values()) {
//					for (RevisionPageInfo revisionPage : revisionPages) {
//						pages.add(revisionPage);
//					}
//				}
//			}
//			
//			return pages;
//		}
		@Override
		public int getOrder() {
			return PRIORITY_NORMAL;
		}

//		@Override
//		public RevisionPageInfo rollbackRevision(PageKey pageKey, int revision) {
//			int v = getCurrentRevision(pageKey) + 1;
//			
//			RevisionPageInfo revisionPage = get(pageKey, revision);
//			
//			RevisionPageInfo page = new RevisionPageInfo();
//			page.setAuthorId(revisionPage.getAuthorId());
//			page.setAuthorName(revisionPage.getAuthorName());
//			page.setComment(revisionPage.getComment());
//			page.setContent(revisionPage.getContent());
//			page.setLastModified(revisionPage.getLastModified());
//			page.setPageKey(revisionPage.getPageKey());
//			page.setRevision(v);
//			page.setTemplateName(revisionPage.getTemplateName());
//			page.setTitle(revisionPage.getTitle());
//			page.setUrlName(revisionPage.getUrlName());
//			
//			return page;
//		}
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
}
