package org.archboy.clobaframe.web.view.tool;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author yang
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml"})
public class MessageSourceToolTest {

	@Inject
	private MessageSourceTool messageSourceTool;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWrite()  {
		// set to english
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		assertEquals(Locale.ENGLISH.toLanguageTag(), messageSourceTool.getLocale());
		
		String code1 = "test.text1";
		String code2 = "test.text2";
		
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		
		String result1 = "This is test text.";
		assertEquals(result1, messageSourceTool.write(code1));
		
		MessageFormat messageFormat1 = new MessageFormat("Hello {0}, now is {1,date,long}.", Locale.ENGLISH);
		String result2 = messageFormat1.format(new Object[]{"foo", now});
		assertEquals(result2, messageSourceTool.write(code2, "foo", now));
		
		// set to simplified Chinese
		LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);
		assertEquals(Locale.SIMPLIFIED_CHINESE.toLanguageTag(), messageSourceTool.getLocale());
		
		String result3 = "这是测试文字。";
		assertEquals(result3, messageSourceTool.write(code1));
		
		MessageFormat messageFormat2 = new MessageFormat("你好 {0}，现在时间 {1,date,long}。", Locale.SIMPLIFIED_CHINESE);
		String result4 = messageFormat2.format(new Object[]{"foo", now});
		assertEquals(result4, messageSourceTool.write(code2, "foo", now));
	}

}
