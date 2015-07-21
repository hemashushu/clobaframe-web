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
public class JsonWriterTest {

	@Inject
	private JsonWriter jsonWriter;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWrite()  {
		assertEquals(StringUtils.EMPTY, jsonWriter.write(null));
		
		User user1 = new User("123456", "foo");
		String json1 = jsonWriter.write(user1);
		assertEquals("{\"id\":\"123456\",\"name\":\"foo\"}", json1);
	}

	public static class User {
		private String id;
		private String name;

		public User() {
		}

		public User(String id, String name) {
			this.id = id;
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
}
