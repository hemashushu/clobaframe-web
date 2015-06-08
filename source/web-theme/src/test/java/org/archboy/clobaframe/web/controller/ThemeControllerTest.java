package org.archboy.clobaframe.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.archboy.clobaframe.web.page.revision.RevisionPageManager;
import org.archboy.clobaframe.webresource.WebResourceManager;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author yang
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration("src/test/resources/webapp")
@ContextConfiguration(locations = { "/webapp/WEB-INF/servlet.xml"})
public class ThemeControllerTest {

	@Inject
	private RevisionPageManager revisionPageManager;
	
	@Inject
	private ResourceLoader resourceLoader;
	
	@Inject
	private WebApplicationContext webApplicationContext;

    private MockMvc mock;

	@Before
	public void setUp() throws Exception {
		this.mock = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetPage() throws Exception {
		// test get page
		mock.perform(get("/page/about").locale(Locale.ENGLISH))
				.andExpect(status().isOk())
				.andExpect(content().string("<h1>about r2</h1>"));
		
		// test get page with locale
		mock.perform(get("/page/about").locale(Locale.SIMPLIFIED_CHINESE))
				.andExpect(status().isOk())
				.andExpect(content().string("<h1>about zh_CN r8</h1>"));
		
		// test get page that with template name spcified.
		mock.perform(get("/page/contact").locale(Locale.ENGLISH))
				.andExpect(status().isOk())
				.andExpect(content().string("<h2>contact[page-mobile]</h2>"));
		
		// test get page by url name
		mock.perform(get("/help/privacy").locale(Locale.ENGLISH))
				.andExpect(status().isOk())
				.andExpect(content().string("<h1>privacy@help/privacy</h1>"));
		
	}

}
