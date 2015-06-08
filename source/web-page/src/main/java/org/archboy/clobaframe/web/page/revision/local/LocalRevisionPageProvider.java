package org.archboy.clobaframe.web.page.revision.local;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.IOUtils;
import org.archboy.clobaframe.io.MimeTypeDetector;
import org.archboy.clobaframe.io.ResourceInfo;
import org.archboy.clobaframe.io.file.ResourceScanner;
import org.archboy.clobaframe.web.page.PageInfo;
import org.archboy.clobaframe.web.page.PageKey;
import org.archboy.clobaframe.web.page.PageProvider;
import org.archboy.clobaframe.web.page.revision.RevisionPageInfo;
import org.archboy.clobaframe.web.page.revision.RevisionPageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * @author yang
 */
@Named
public class LocalRevisionPageProvider implements RevisionPageProvider {

	@Inject
	private ResourceLoader resourceLoader;

	@Inject
	private MimeTypeDetector mimeTypeDetector;
	
	@Inject
	private ResourceScanner resourceScanner;
	
	// local resource path, usually relative to the 'src/main/webapp' folder.
	// to using this repository, the web application war package must be expended when running.
	private static final String DEFAULT_PAGE_RESOURCE_PATH = "" ; //"page";
	
	@Value("${clobaframe.web.page.resource.path:" + DEFAULT_PAGE_RESOURCE_PATH + "}")
	private String pageResourcePath;
	
	/**
	 * The resource name can specify some properties, includes language code,
	 * revision number, rendering template name.
	 * 
	 * Note:
	 * Resources with the same name must keep the same URL name and template name also.
	 * 
	 * Example:
	 * 
	 * terms.md // default English language
	 * terms_ja.md // for Japanese language
	 * terms_zh_CN.md // for Simplified Chinese language
	 * terms.r23.md // the revision 23
	 * terms@path#name.md // using the specify URL "path/name" to override the default page URL.
	 *						// the slash mark is replaced with the hash mask.
	 * terms[templatepath#name].md // using the template "templatepath/name" for rendering, 
	 *								//the slash mark is replaced with the hash mark.
	 * A full name example:
	 * terms@developers#api[template#dev]_zh_CN.r23.md
	 * 
	 * Regex match groups:
	 * 1 - name
	 * 3 - custom URL name (optional)
	 * 5 - template name (optional)
	 * 7 - lang code (optional)
	 * 10 - country code (optional)
	 * 12 - revision (optional)
	 
	 * 
	 */
	private static final String resourceNameRegex = 
			"^([a-zA-Z0-9-]+)(@([a-zA-Z0-9#-]+))?(\\[([a-zA-Z0-9#-]+)\\])?(_([a-z]{2})((_)([A-Z]{2}))?)?(.r(\\d+))?\\.md$";
	
	private Pattern resourceNamePattern = Pattern.compile(resourceNameRegex);
	
	private static final String DEFAULT_LOCALE = "en";
	
	@Value("${clobaframe.web.page.defaultLocale:" + DEFAULT_LOCALE + "}")
	private Locale defaultLocale;

	// content header
	private static final String regexHeader1a = "^(.+)\\n(={3,})(\\n|$)";
	private static final String regexHeader1b = "^(#.+#?)(\\n|$)";
	
	private static final Pattern patternHeader1a = Pattern.compile(regexHeader1a);
	private static final Pattern patternHeader1b = Pattern.compile(regexHeader1b);

	private final Logger logger = LoggerFactory.getLogger(LocalRevisionPageProvider.class);
	
	@Override
	public Collection<PageInfo> getAll() {
		Resource resource = resourceLoader.getResource(pageResourcePath);
		
		try{
			File baseDir = resource.getFile();
			
			// Do not throws exception because the web application maybe running in the
			// WAR package.
			if (!baseDir.exists()){
				logger.error("Can not find the local page resource folder [{}], please ensure " +
						"unpackage the WAR if you are running web application.", pageResourcePath);
				return new ArrayList<PageInfo>();
			}
			
			return scan(baseDir);
			
		}catch(IOException e){
			logger.error("Load local page resource repository error, {}", e.getMessage());
		}
		
		return new ArrayList<PageInfo>();
	}
	
	private Collection<PageInfo> scan(File baseDir) {
		
		LocalRevisionPageResourceNameStrategy localPageResourceNameStrategy = new DefaultLocalRevisionPageResourceNameStrategy(baseDir);
		LocalRevisionPageResourceInfoFactory localPageResourceInfoFactory = new LocalRevisionPageResourceInfoFactory(mimeTypeDetector, localPageResourceNameStrategy);
		
		List<PageInfo> pages = new ArrayList<PageInfo>();
		
		Collection<ResourceInfo> resourceInfos = resourceScanner.scan(baseDir, localPageResourceInfoFactory);
		for(ResourceInfo resourceInfo : resourceInfos) {
			LocalRevisionPageResourceInfo localPageResourceInfo = (LocalRevisionPageResourceInfo)resourceInfo;
			String fullname = localPageResourceInfo.getName();
			String path = null;
			String filename = fullname;
			
			int pos = fullname.lastIndexOf('/');
			if (pos > 0) {
				path = fullname.substring(0, pos);
				filename = fullname.substring(pos + 1);
			}
			
			Matcher matcher = resourceNamePattern.matcher(filename);
			if (matcher.find()){
				String name = matcher.group(1);
				String urlName = matcher.group(3);
				String templateName = matcher.group(5);
				String langCode = matcher.group(7);
				String countryCode = matcher.group(10);
				String revision = matcher.group(12);
				
				// build page name
				String pageName = (path != null) ? pageName = path + "/" + name : name;
				
				// get the locale value
				Locale locale = null;

				if (langCode != null && countryCode != null){
					locale = new Locale(langCode, countryCode);
				}else if (langCode != null){
					locale = new Locale(langCode);
				}else{
					locale = defaultLocale;
				}

				// get the revision number.
				// default is "0" (while no specified).
				int revisionNumber = 0;
				if (revision != null) {
					revisionNumber = Integer.parseInt(revision);
				}
				
				// convert the hash mark
				if (urlName != null) {
					urlName = urlName.replaceAll("#", "/");
				}
				
				// convert the hash mark
				if (templateName != null) {
					templateName = templateName.replaceAll("#", "/");
				}
				
				try{
					RevisionPageInfo revisionDoc = convertResourceInfo(
								resourceInfo, 
								pageName, locale, revisionNumber, 
								urlName, templateName);
				
					pages.add(revisionDoc);
				}catch(IOException e){
					// ignore
				}
			}
		}
		
		return pages;
	}
	
	private RevisionPageInfo convertResourceInfo(ResourceInfo resourceInfo, 
			String name, Locale locale, int revision,
			String urlName, String templateName) throws IOException{
		
		InputStream in = null;
		try{
			in = resourceInfo.getContent();
			String content = IOUtils.toString(in, "UTF-8");
			String title = extractTitle(content);
			
			RevisionPageInfo page = new RevisionPageInfo();
			page.setContent(content);
			page.setLastModified(resourceInfo.getLastModified());
			page.setPageKey(new PageKey(name, locale));
			page.setRevision(revision);
			page.setTemplateName(templateName);
			page.setTitle(title);
			page.setUrlName(urlName);
			
			return page;
			
		}finally{
			IOUtils.closeQuietly(in);
		}
	}
	
	/**
	 * Get the first line as page title.
	 * @param text
	 * @return 
	 */
	private String extractTitle(String text) {
		/**
		 * #TITLE#\n
		 * CONTENT
		 * 
		 * - OR -
		 * 
		 * TITLE\n
		 * ===\n
		 * CONTENT
		 * 
		 * */
		Matcher matcher1a = patternHeader1a.matcher(text);
		if (matcher1a.find()){
			String title = matcher1a.group(1);
//			String content = null;
//			int start = matcher1a.end();
//			if (start < text.length()) {
//				content = text.substring(start).trim();
//			}
			return title;
		}
		
		Matcher matcher1b = patternHeader1b.matcher(text);
		if (matcher1b.find()){
			String title = trimHeaderHashSymbol(matcher1b.group(1));
//			String content = null;
//			int start = matcher1b.end();
//			if (start < text.length()) {
//				content = text.substring(start).trim();
//			}
			return title;
		}

		throw new IllegalArgumentException("Can not find the Markdown document title.");
	}

	/**
	 * Remove the prefix and suffix hash marks.
	 * @param title
	 * @return 
	 */
	private String trimHeaderHashSymbol(String title) {
		int start = 0;
		int end = title.length() - 1;
		for (; start < end; start ++) {
			if (title.charAt(start) != '#') break;
		}
		
		for (; end > start; end--){
			if (title.charAt(end) != '#') break;
		}
		
		return title.substring(start, end +1).trim();
	}	

	@Override
	public int getOrder() {
		return PRIORITY_LOWER;
	}

	@Override
	public RevisionPageInfo get(PageKey pageKey, int revision) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Collection<RevisionPageInfo> listRevision(PageKey pageKey) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
