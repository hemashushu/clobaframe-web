package org.archboy.clobaframe.web.theme;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;
import org.archboy.clobaframe.resource.ResourceManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

/**
 *
 * @author yang
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext.xml", "/webContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration("src/test/resources/webapp")
@ContextConfiguration(locations = { "/webapp/WEB-INF/servlet.xml"})
public class ThemeManagerTest {

	@Inject
	private ThemeManager themeManager;

	@Inject
	private ResourceManager resourceManager;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testListBase()  {
		
		// list base
		Collection<ThemePackage> packages1 = themeManager.list(ThemeManager.PACKAGE_CATALOG_BASE);
		assertEquals(1, packages1.size());
		
		ThemePackage basePackage = packages1.iterator().next();
		assertEquals(ThemeManager.PACKAGE_ID_BASE, basePackage.getId());
		
		// test get package info
		assertNull(basePackage.getAuthorName());
		assertEquals(ThemeManager.PACKAGE_CATALOG_BASE, basePackage.getCatalog());
		assertNull(basePackage.getDescription());
		assertNull(basePackage.getLastModified());
		assertEquals(ThemeManager.PACKAGE_ID_BASE, basePackage.getId());
		assertNull(basePackage.getVersion());
		assertNull(basePackage.getWebsite());
		
		// test get resource
		List<String> imageCssJs1 = Arrays.asList("resource/css/common.css", "resource/css/index.css",
				"resource/image/loading-16x16.gif", "resource/js/index.js", 
				"resource/js/jquery-1.11.1.js","resource/js/jquery-1.11.1.min.js",
				"resource/js/i18n/messages.js", "resource/js/i18n/messages_zh_CN.js");
		
		List<String> templates1 = Arrays.asList("template/index.vm", "template/layout/default.vm",
				"template/share/footer.vm", "template/share/header.vm");
		
		// get all resources.
		Collection<ThemeResourceInfo> themeResourceInfos1 = basePackage.listResource();
		
		for (String name : imageCssJs1) {
			boolean found = false;
			for(ThemeResourceInfo info : themeResourceInfos1) {
				if (info.getName().equals(name) && info.getContentType() == ThemeResourceInfo.TYPE_RESOURCE) {
					found = true;
					break;
				}
			}
			
			assertTrue(found);
		}
		
		for (String name : templates1) {
			boolean found = false;
			for(ThemeResourceInfo info : themeResourceInfos1) {
				if (info.getName().equals(name) && info.getContentType() == ThemeResourceInfo.TYPE_TEMPLATE) {
					//if (info.isTemplate()) {
						found = true;
						break;
					//}
				}
			}
			
			assertTrue(found);
		}
		
		for (String name : imageCssJs1) {
			assertNotNull(basePackage.getResource(name));
		}
		
		for (String name : templates1) {
			assertNotNull(basePackage.getResource(name));
		}
		
		// get get none-exist resource
		assertNull(basePackage.getResource("none-exists"));
		
		// test get web resource
		for (String name : imageCssJs1) {
			String webResourceName = name.substring("resource/".length());
			assertNotNull(resourceManager.get(webResourceName));
		}
		
		// get package by name
		ThemePackage themePackage1 = themeManager.get(ThemeManager.PACKAGE_CATALOG_BASE, 
				ThemeManager.PACKAGE_ID_BASE);
		
		assertEquals(basePackage, themePackage1);
		
		// get none-exist package
		Assert.isNull(themeManager.get(
				ThemeManager.PACKAGE_CATALOG_BASE, 
				"none-exists"));
	}
	
	@Test
	public void testListLocal() throws ParseException  {
		// test list all local package
		Collection<ThemePackage> packages1 = themeManager.list(ThemeManager.PACKAGE_CATALOG_LOCAL);
		assertEquals(2, packages1.size());
		
		List<String> packageNames1 = Arrays.asList("dark", "flat");
		
		for(ThemePackage themePackage : packages1) {
			assertTrue(packageNames1.contains(themePackage.getId()));
		}
		
		// test get info
		String dateString1 = "2015-06-21T18:35:30.555Z";
		DateFormat dateFormat1 = new ISO8601DateFormat();
		Date date1 = dateFormat1.parse(dateString1);
		
		ThemePackage themePackage1 = themeManager.get(ThemeManager.PACKAGE_CATALOG_LOCAL, "dark");
		assertEquals("yang", themePackage1.getAuthorName());
		assertEquals(ThemeManager.PACKAGE_CATALOG_LOCAL, themePackage1.getCatalog());
		assertEquals("a test theme", themePackage1.getDescription());
		assertDateEquals(date1, themePackage1.getLastModified());
		assertEquals("The Dark Theme", themePackage1.getTitle());
		assertEquals("dark", themePackage1.getId());
		assertEquals("1.0.1", themePackage1.getVersion());
		assertEquals("http://archboy.org", themePackage1.getWebsite());
		
		// test get all resource
		List<String> resources1 = Arrays.asList(
				"info.json",
				"resource/css/dark.css", 
				"resource/css/index.css",
				"resource/image/dark.png", 
				"resource/js/dark.js", 
				"template/index.vm");
		
		Collection<ThemeResourceInfo> themeResourceInfos1 = themePackage1.listResource();
		
		for (String name : resources1) {
			boolean found = false;
			for(ThemeResourceInfo info : themeResourceInfos1) {
				if (info.getName().equals(name)) {
					found = true;
					break;
				}
			}
			
			assertTrue(found);
		}
		
		for (String name : resources1) {
			assertNotNull(themePackage1.getResource(name));
		}
		
		// get get none-exist resource
		assertNull(themePackage1.getResource("none-exists"));
		
		// test get web resource
		for (String name : resources1) {
			String resourceName = "theme/dark/" + name;
			assertNotNull(resourceManager.get(resourceName));
		}
	}

	private static void assertDateEquals(Date expected, Date actual){
		if (expected == null && actual == null){
			//
		}else if(expected == null || actual == null){
			fail("date not equals");
		}else{
			assertTrue(Math.abs(expected.getTime() - actual.getTime()) < 1000 );
		}
	}
}
