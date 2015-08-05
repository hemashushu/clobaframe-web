package org.archboy.clobaframe.web.controller;

import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;
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
public class PageControllerTest {

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
				.andExpect(content().string("<h1>about zh-CN r8</h1>"));
		
		// test get page with locale that does not exists
		mock.perform(get("/page/about").locale(Locale.GERMANY))
				.andExpect(status().isOk())
				.andExpect(content().string("<h1>about r2</h1>"));
		
		// test get page with locale and prefer locale
		mock.perform(get("/page/terms").locale(Locale.JAPANESE)
				.param("locale", "zh_CN"))
				.andExpect(status().isOk())
				.andExpect(content().string("<h3>terms zh-CN r24</h3>"));
		
		// test get page with prefer locale that does not exist
		mock.perform(get("/page/terms").locale(Locale.JAPANESE)
				.param("locale", "de"))
				.andExpect(status().isNotFound());
		
		// test get page with prefer locale and revision
		mock.perform(get("/page/terms").locale(Locale.JAPANESE)
				.param("locale", "zh_CN")
				.param("revision", "23"))
				.andExpect(status().isOk())
				.andExpect(content().string("<h3>terms zh-CN r23</h3>"));
		
		// test get page with prefer locale and revision that does not found
		mock.perform(get("/page/terms").locale(Locale.JAPANESE)
				.param("locale", "zh_CN")
				.param("revision", "28"))
				.andExpect(status().isNotFound());
		
		// test get page that with template name spcified.
		mock.perform(get("/page/contact").locale(Locale.ENGLISH))
				.andExpect(status().isOk())
				.andExpect(content().string("<h2>contact[page-mobile]</h2>"));
		
		mock.perform(get("/page/none-exists").locale(Locale.ENGLISH))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void testGetPageByUrlName() throws Exception {	
		// test get page by url name
		mock.perform(get("/help/privacy").locale(Locale.ENGLISH))
				.andExpect(status().isOk())
				.andExpect(content().string("<h1>privacy@help/privacy</h1>"));
		
		// test get page by url name and prefer locale
		mock.perform(get("/help/terms").locale(Locale.ENGLISH)
				.param("locale", "zh_CN"))
				.andExpect(status().isOk())
				.andExpect(content().string("<h3>terms zh-CN r24</h3>"));
		
		// test get page by url name and prefer locale that does not exist
		mock.perform(get("/help/terms").locale(Locale.ENGLISH)
				.param("locale", "de"))
				.andExpect(status().isNotFound());
		
		// test get page with prefer locale and revision
		mock.perform(get("/help/terms").locale(Locale.JAPANESE)
				.param("locale", "zh_CN")
				.param("revision", "23"))
				.andExpect(status().isOk())
				.andExpect(content().string("<h3>terms zh-CN r23</h3>"));
		
		// test get page with prefer locale and revision that does not found
		mock.perform(get("/help/terms").locale(Locale.JAPANESE)
				.param("locale", "zh_CN")
				.param("revision", "28"))
				.andExpect(status().isNotFound());
		
		// test get page by url name that does not exists
		mock.perform(get("/help/none-exists").locale(Locale.ENGLISH))
				.andExpect(status().isNotFound());
		
		
	}

}
