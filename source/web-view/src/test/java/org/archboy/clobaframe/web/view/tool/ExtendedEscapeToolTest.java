package org.archboy.clobaframe.web.view.tool;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;
import org.archboy.clobaframe.web.view.tool.impl.ExtendedEscapeTool;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

/**
 *
 * @author yang
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml"})
public class ExtendedEscapeToolTest {

	@Inject
	private ExtendedEscapeTool extendedEscapeTool;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHtml()  {
		String text1 = "hello <foo> & <bar>";
		String result1 = extendedEscapeTool.html(text1);
		assertEquals("hello &lt;foo&gt; &amp; &lt;bar&gt;", result1);
	}

	@Test
	public void testHtmlP()  {
		String text1 = "hello <foo> & <bar>\n\nworld\nend";
		String result1 = extendedEscapeTool.htmlP(text1);
		assertEquals("hello &lt;foo&gt; &amp; &lt;bar&gt;<p>world<br>end", result1);
	}
	
}
