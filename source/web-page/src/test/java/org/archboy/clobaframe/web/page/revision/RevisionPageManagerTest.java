package org.archboy.clobaframe.web.page.revision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.web.page.Page;
import org.archboy.clobaframe.web.page.PageKey;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

/**
 *
 * @author yang
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml"})
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
	public void testGet()  {
		 // revisionPageManager.get(new PageKey("about", Locale.ENGLISH));
		Collection<RevisionPage> pages1 = revisionPageManager.listRevision(new PageKey("about", Locale.ENGLISH));
		for(RevisionPage p : pages1){
			System.out.println("key:" + p.getPageKey() + ", rev:" + p.getRevision());
		}
	}

	//SS@Named
	public static class InMemoryRevisionPageProviderAndRepository implements RevisionPageProvider, RevisionPageRepository {

		
		@Override
		public RevisionPage get(PageKey pageKey, int revision) {
			return null;
		}

		@Override
		public Collection<RevisionPage> listRevision(PageKey pageKey) {
			return null;
		}

		@Override
		public Collection<Page> getAll() {
			return new ArrayList<Page>();
		}

		@Override
		public int getOrder() {
			return PRIORITY_HIGH;
		}

		@Override
		public int getCurrentRevision(PageKey pageKey) {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}

		@Override
		public void rollbackRevision(PageKey pageKey, int revision) {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}

		@Override
		public void delete(PageKey pageKey, int revision) {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}

		@Override
		public Page update(PageKey pageKey, String title, String content, String urlName, String templateName, String authorName, String authorId, String comment) {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}

		@Override
		public void delete(PageKey pageKey) {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}
	}
	
}
