package org.archboy.clobaframe.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.IOUtils;
import org.archboy.clobaframe.setting.common.global.GlobalSettingProvider;
import org.archboy.clobaframe.setting.common.global.GlobalSettingRepository;
import org.archboy.clobaframe.setting.support.Utils;
import org.archboy.clobaframe.web.theme.ThemeManager;
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
	private ThemeManager themeManager;
	
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
		// test get index
		mock.perform(get("/changetheme"))
				.andExpect(status().isOk())
				.andExpect(content().string(
						"<!DOCTYPE html>\n" +
						"<head>\n" +
						"<script src=\"/resource/js/index.js?v4a6ae5f4\"></script>\n" +
						"\n" +
						"</head>"));
		
		// test get index
		mock.perform(get("/changetheme").param("name", "dark"))
				.andExpect(status().isOk())
				.andExpect(content().string("<!DOCTYPE html>\n" +
						"<head>\n" +
						"<script src=\"/resource/js/index.js?v4a6ae5f4\"></script>\n" +
						"<link rel=\"stylesheet\" data-source=\"theme\" data-catalog=\"local\" data-id=\"dark\" href=\"/resource/theme/dark/resource/css/dark.css?vbf81ee39\"><link rel=\"stylesheet\" data-source=\"theme\" data-catalog=\"local\" data-id=\"dark\" href=\"/resource/theme/dark/resource/css/index.css?v6dc92db3\">\n" +
						"</head>"));
		
		// test get index
		mock.perform(get("/changetheme").param("name", "flat"))
				.andExpect(status().isOk())
				.andExpect(content().string("<!DOCTYPE html>\n" +
						"<head>\n" +
						"<script src=\"/resource/js/index.js?v4a6ae5f4\"></script>\n" +
						"<link rel=\"stylesheet\" data-source=\"theme\" data-catalog=\"local\" data-id=\"flat\" href=\"/resource/theme/flat/resource/css/flat.css?vc724b117\">\n" +
						"</head>"));
		
	}

	@Named
	public static class TestingGlobalSettingRepository implements GlobalSettingProvider, GlobalSettingRepository {

		private Map<String, Object> setting = new LinkedHashMap<String, Object>();
		
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
