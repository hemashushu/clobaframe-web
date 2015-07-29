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
public class DateTimeFormatterTest {

	@Inject
	private DateTimeFormatter dateTimeFormatter;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDate()  {
		
	}
	
	@Test
	public void testTime()  {
		
	}
	
	@Test
	public void testDateTime()  {
		
	}

}
