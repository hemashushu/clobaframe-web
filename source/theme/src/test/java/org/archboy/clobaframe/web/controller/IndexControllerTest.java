package org.archboy.clobaframe.web.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.setting.global.GlobalSettingProvider;
import org.archboy.clobaframe.setting.global.GlobalSettingRepository;
import org.archboy.clobaframe.setting.support.Utils;
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
public class IndexControllerTest {
	
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
		// set no theme
		mock.perform(get("/settheme"))
				.andExpect(status().isOk())
				.andExpect(content().string("[]"));
		
		// test get index
		mock.perform(get("/index"))
				.andExpect(status().isOk())
				.andExpect(content().string(
						"<!DOCTYPE html>\n" +
						"<head>\n" +
						"<link href=\"/resource/css/common.css?v0ef59f0a\" rel=\"stylesheet\">\n" + 
						"<script src=\"/resource/js/index.js?v4a6ae5f4\"></script>\n" +
						"<script src=\"/resource/js/i18n/messages.js?v565ad0a3\"></script>\n" +
						"</head>\n" + 
						"<body><h1>Index</h1></body>"));
		
		// It seems mock can not catch the exception
		//mock.perform(get("/page"))
		//		.andExpect(status().isNotFound());
		
		// NOTE::
		// to test the theme changing, it's needed to turn off the template engine cache.
		
		// set theme to "dark"
		mock.perform(get("/settheme").param("name", "dark"))
				.andExpect(status().isOk())
				.andExpect(content().string(
						"[\"<link href=\\\"/resource/theme/dark/resource/css/dark.css?vbf81ee39\\\" rel=\\\"stylesheet\\\" data-source=\\\"theme\\\" data-catalog=\\\"local\\\" data-id=\\\"dark\\\">\",\"<script src=\\\"/resource/theme/dark/resource/js/dark.js?vc8acb1e1\\\" data-source=\\\"theme\\\" data-catalog=\\\"local\\\" data-id=\\\"dark\\\"></script>\"]"));
		
		// test get index that has been overwrite.
		mock.perform(get("/index"))
				.andExpect(status().isOk())
				.andExpect(content().string(				
						"<!DOCTYPE html>\n" +
						"<h1 class=\"dark\">Index</h1>"));
		
		// set theme to "flat"
		mock.perform(get("/settheme").param("name", "flat"))
				.andExpect(status().isOk())
				.andExpect(content().string("[]"));
	
		// test get index, that will return original template
		mock.perform(get("/index"))
				.andExpect(status().isOk())
				.andExpect(content().string(
						"<!DOCTYPE html>\n" +
						"<head>\n" +
						"<link href=\"/resource/css/common.css?v0ef59f0a\" rel=\"stylesheet\">\n" +	
						"<script src=\"/resource/js/index.js?v4a6ae5f4\"></script>\n" +
						"<script src=\"/resource/js/i18n/messages.js?v565ad0a3\"></script>\n" +
						"</head>\n" + 
						"<body><h1>Index</h1></body>"));
		
		// test get a new page
		mock.perform(get("/page"))
				.andExpect(status().isOk())
				.andExpect(content().string(				
						"<!DOCTYPE html>\n" +
						"<head>\n" +
						"<link href=\"/resource/css/common.css?v0ef59f0a\" rel=\"stylesheet\">\n" +	
						"\n" +
						"<script src=\"/resource/js/i18n/messages.js?v565ad0a3\"></script>\n" +
						"</head>\n" + 
						"<body><main>Page</main></body>"));
	}

	@Named
	public static class TestingGlobalSettingRepository implements GlobalSettingProvider, GlobalSettingRepository {

		private Map<String, Object> setting = new LinkedHashMap<String, Object>();

		@Override
		public String getName() {
			return "tesingGlobalSettingRepository";
		}
		
		@Override
		public int getOrder() {
			return PRIORITY_HIGH;
		}

		@Override
		public Map<String, Object> list() {
			return setting;
		}
		
		@Override
		public void update(Map<String, Object> item) {
			setting = Utils.merge(setting, item);
		}

		@Override
		public void update(String key, Object value) {
			setting = Utils.merge(setting, key, value);
		}
	}
	
}
