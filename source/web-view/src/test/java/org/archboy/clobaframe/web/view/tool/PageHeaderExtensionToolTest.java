package org.archboy.clobaframe.web.view.tool;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
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
import org.springframework.web.context.request.AbstractRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author yang
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml"})
public class PageHeaderExtensionToolTest {

	@Inject
	private PageHeaderTool pageHeaderTool;

	@Inject
	private PageHeaderExtensionTool pageHeaderExtensionTool;
	
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
		HttpServletRequest httpServletRequest = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));
		
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		List<String> headers1 = pageHeaderExtensionTool.list();
		assertEquals(1, headers1.size());
		assertEquals(pageHeaderTool.writeResource("js/i18n/messages.js"), headers1.get(0));
		
		assertEquals(pageHeaderTool.writeResource("js/i18n/messages.js"),
				pageHeaderExtensionTool.write());
		
		// change locale
		LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);
		
		List<String> headers2 = pageHeaderExtensionTool.list();
		assertEquals(2, headers2.size());
		assertEquals(pageHeaderTool.writeResource("js/i18n/messages.js"), headers2.get(0));
		assertEquals(pageHeaderTool.writeResource("js/i18n/messages_zh_CN.js"), headers2.get(1));
		
		assertEquals(
				pageHeaderTool.writeResource("js/i18n/messages.js") + 
				pageHeaderTool.writeResource("js/i18n/messages_zh_CN.js"),
				pageHeaderExtensionTool.write());
		
		assertEquals(
				pageHeaderTool.writeResource("js/i18n/messages.js") + 
				"\n" +
				pageHeaderTool.writeResource("js/i18n/messages_zh_CN.js"),
				pageHeaderExtensionTool.write("\n"));
		
		// test add header
		// expect: "<meta charset=\"UTF-8\">"
		Map<String, Object> attr1 = new LinkedHashMap<String, Object>();
		attr1.put("charset", "UTF-8");
		
		pageHeaderExtensionTool.add("meta", attr1, false);
		List<String> headers3 = pageHeaderExtensionTool.list();
		assertEquals(3, headers3.size());
		assertEquals("<meta charset=\"UTF-8\">", headers3.get(2));
		
	}

}
