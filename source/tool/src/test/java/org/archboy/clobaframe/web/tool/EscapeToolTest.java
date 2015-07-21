package org.archboy.clobaframe.web.tool;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

/**
 *
 * @author yang
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml"})
public class EscapeToolTest {

	@Inject
	private EscapeTool escapeTool;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHtml()  {
		assertEquals(StringUtils.EMPTY, escapeTool.html(null));
		
		String text1 = "hello <foo> & <bar>";
		String result1 = escapeTool.html(text1);
		assertEquals("hello &lt;foo&gt; &amp; &lt;bar&gt;", result1);
	}

	@Test
	public void testHtmlP()  {
		assertEquals(StringUtils.EMPTY, escapeTool.htmlP(null));
		
		String text1 = "hello <foo> & <bar>\n\nworld\nend";
		String result1 = escapeTool.htmlP(text1);
		assertEquals("<p>hello &lt;foo&gt; &amp; &lt;bar&gt;</p><p>world</p><p>end</p>", result1);
		
		String text2 = "hello world";
		String result2 = escapeTool.htmlP(text2);
		assertEquals("hello world", result2);
	}
	
	@Test
	public void testJavascript() {
		assertEquals(StringUtils.EMPTY, escapeTool.javascript(null));
		
		String text1 = "I'm foo, hello \"bar\"";
		String result1 = escapeTool.javascript(text1);
		assertEquals("I\\'m foo, hello \\\"bar\\\"", result1);
	}
	
	@Test
	public void testUrl() {
		assertEquals(StringUtils.EMPTY, escapeTool.url(null));
		
		String text1 = "hello here & there";
		String result1 = escapeTool.url(text1);
		assertEquals("hello+here+%26+there", result1);
	}
}
