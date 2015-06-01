package org.archboy.clobaframe.web.view.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;
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
public class ResourcesControllerTest {

	@Inject
	private WebResourceManager webResourceManager;
	
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
	public void testSendResource() throws Exception {
		String location1 = webResourceManager.getLocation("css/index.css");
		String location2 = webResourceManager.getLocation("root/robots.txt");
		String location3 = webResourceManager.getLocation("root/favicon-16x16.ico");
		String location4 = webResourceManager.getLocation("root/favicon-16x16.png");
		String location5 = webResourceManager.getLocation("root/apple-touch-icon-120x120.png");
		String location6 = webResourceManager.getLocation("root/launcher-icon-192x192.png");
		
		mock.perform(get(location1))
				.andExpect(status().isOk())
				.andExpect(content().string(getFileTextContent("resources/css/index.css")));
		
		mock.perform(get(location2))
				.andExpect(status().isOk())
				.andExpect(content().bytes(getFileContent("resources/root/robots.txt")));
		
		mock.perform(get(location3))
				.andExpect(status().isOk())
				.andExpect(content().bytes(getFileContent("resources/root/favicon-16x16.ico")));
		
		mock.perform(get(location4))
				.andExpect(status().isOk())
				.andExpect(content().bytes(getFileContent("resources/root/favicon-16x16.png")));
		
		mock.perform(get(location5))
				.andExpect(status().isOk())
				.andExpect(content().bytes(getFileContent("resources/root/apple-touch-icon-120x120.png")));
		
		mock.perform(get(location6))
				.andExpect(status().isOk())
				.andExpect(content().bytes(getFileContent("resources/root/launcher-icon-192x192.png")));
	}

	private String getFileTextContent(String name) throws IOException {
		byte[] content = getFileContent(name);
		return new String(content, "UTF-8");
	}
	
	/**
	 *
	 * @param name Relate to the 'src/test/resources' folder.
	 * @return
	 * @throws IOException
	 */
	private byte[] getFileContent(String name) throws IOException {
		File file = getFileByName(name);
		InputStream in = new FileInputStream(file);
		byte[] data = IOUtils.toByteArray(in);
		in.close();
		return data;
	}

	/**
	 * Get the test resources by file name.
	 *
	 * @param name Relate to the 'src/test/resources' folder.
	 * @return
	 * @throws IOException
	 */
	private File getFileByName(String name) throws IOException{
		Resource resource = resourceLoader.getResource(name); //"file:target/test-classes/" +
		return resource.getFile();
	}	
}
