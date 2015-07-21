package org.archboy.clobaframe.web.tool;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author yang
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml"})
public class PageHeaderWriterTest {

	@Inject
	private PageHeaderTool pageHeaderTool;

	@Inject
	private PageHeaderWriter pageHeaderWriter;
	
	@Inject
	private ContextPageHeaderProvider contextPageHeaderProvider;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWrite()  {
	
		// test list
		// build mock
		HttpServletRequest httpServletRequest1 = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest1));
		
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		assertEquals(
				pageHeaderTool.writeResource("js/i18n/messages.js"),
				pageHeaderWriter.write());
		
		// change locale
		LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);
		
		assertEquals(
				pageHeaderTool.writeResource("js/i18n/messages.js") + 
				pageHeaderTool.writeResource("js/i18n/messages_zh_CN.js"),
				pageHeaderWriter.write());
		
		assertEquals(
				pageHeaderTool.writeResource("js/i18n/messages.js") + 
				"\n" +
				pageHeaderTool.writeResource("js/i18n/messages_zh_CN.js"),
				pageHeaderWriter.write("\n"));
		
		// test add header
		// expect: "<meta charset=\"UTF-8\">"
		Map<String, Object> attr1 = new LinkedHashMap<String, Object>();
		attr1.put("charset", "UTF-8");
		
		contextPageHeaderProvider.add("meta", attr1, false);
		
		assertEquals(
				"<meta charset=\"UTF-8\">" +
				pageHeaderTool.writeResource("js/i18n/messages.js") + 
				pageHeaderTool.writeResource("js/i18n/messages_zh_CN.js"),
				pageHeaderWriter.write());
		
		HttpServletRequest httpServletRequest2 = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest2));
		
		Map<String, Object> attr2 = new LinkedHashMap<String, Object>();
		attr2.put("name", "robots");
		attr2.put("content", "noodp");
		
		contextPageHeaderProvider.add("meta", attr2, false);
		assertEquals(
				"<meta name=\"robots\" content=\"noodp\">" +
				pageHeaderTool.writeResource("js/i18n/messages.js") + 
				pageHeaderTool.writeResource("js/i18n/messages_zh_CN.js"),
				pageHeaderWriter.write());
		
	}

}
