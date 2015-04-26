package org.archboy.clobaframe.web.page.revision.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.IOUtils;
import org.archboy.clobaframe.web.page.Page;
import org.archboy.clobaframe.web.page.PageProvider;
import org.archboy.clobaframe.web.page.revision.RevisionPage;
import org.archboy.clobaframe.webresource.WebResourceInfo;
import org.archboy.clobaframe.webresource.WebResourceManager;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author yang
 */
@Named
public class WebResourcePageProvider implements PageProvider {

	private static final String DEFAULT_PAGE_RESOURCE_PATH = "page";
	
	@Value("${clobaframe.web.page.resource.path}")
	private String pageResourcePath = DEFAULT_PAGE_RESOURCE_PATH;
	
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
	 * terms@path#name.md // using the specify URL to override the default page URL.
	 *						// the slash mark is replaced with the hash mask.
	 * terms[template#name].md // using the template "doc-view/default" for rendering, 
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
	private static final String pageResourceNameRegex = 
			"^([a-zA-Z0-9-]+)(@([a-zA-Z0-9#-]+))?(\\[([a-zA-Z0-9#-]+)\\])?(_([a-z]{2})((_)([A-Z]{2}))?)?(.r(\\d+))?\\.md$";
	private Pattern pageResourceNamePattern = Pattern.compile(pageResourceNameRegex);
	
	@Inject
	private WebResourceManager webResourceManager;
	
	private static final String DEFAULT_LOCALE_NAME = "en";
	
	@Value("${clobaframe.web.page.defaultLocale}")
	private String localeName = DEFAULT_LOCALE_NAME;
	
	private Locale defaultLocale;
	
	private static final String regexHeader1a = "^(.+)\\n(={3,})(\\n|$)";
	private static final String regexHeader1b = "^(#.+#?)(\\n|$)";
	
	private static final Pattern patternHeader1a = Pattern.compile(regexHeader1a);
	private static final Pattern patternHeader1b = Pattern.compile(regexHeader1b);

	@PostConstruct
	public void init(){
		defaultLocale = Locale.forLanguageTag(localeName);
	} 
	
	@Override
	public int getPriority() {
		return PRIORITY_NORMAL;
	}
	
	@Override
	public Collection<Page> getAll() {
		List<Page> pages = new ArrayList<Page>();
		
		String basePath = pageResourcePath + "/";
		
		// find all script message resource.
		Collection<String> resourceNames = webResourceManager.getAllNames();

		for(String resourceName : resourceNames){

			if (!resourceName.startsWith(basePath)) {
				continue;
			}
			
			// the fullname will excludes the slash mark.
			// e.g. "developers/api/user.md"
			String fullname = resourceName.substring(basePath.length() + 1); 
			String path = null;
			String filename = fullname;
			
			int pos = fullname.lastIndexOf('/');
			if (pos > 0) {
				path = fullname.substring(0, pos);
				filename = fullname.substring(pos + 1);
			}
			
			Matcher matcher = pageResourceNamePattern.matcher(filename);
			if (matcher.find()){
				String name = matcher.group(1);
				String urlName = matcher.group(3);
				String templateName = matcher.group(5);
				String langCode = matcher.group(7);
				String countryCode = matcher.group(10);
				String revision = matcher.group(12);
				
				// concat page name
				String pageName = name;
				if (path != null) {
					pageName = path + "/" + name;
				}
				
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
					WebResourceInfo webResourceInfo = webResourceManager.getResource(resourceName);
					
					RevisionPage revisionDoc = convertResourceInfo(
								webResourceInfo, 
								name, locale, revisionNumber, 
								urlName, templateName);
				
					pages.add(revisionDoc);
				}catch(IOException e){
					// ignore
				}
			}
		}
		
		return pages;
	}
	
	private RevisionPage convertResourceInfo(WebResourceInfo webResourceInfo, 
			String name, Locale locale, int revision,
			String urlName, String templateName) throws IOException{
		
		InputStream in = null;
		try{
			in = webResourceInfo.getContent();
			String content = IOUtils.toString(in, "UTF-8");
			String title = extractTitle(content);
			
			RevisionPage page = new RevisionPage();
			page.setContent(content);
			page.setLastModified(webResourceInfo.getLastModified());
			page.setLocale(locale);
			page.setName(name);
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
}
