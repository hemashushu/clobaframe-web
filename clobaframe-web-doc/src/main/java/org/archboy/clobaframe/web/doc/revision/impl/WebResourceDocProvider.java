package org.archboy.clobaframe.web.doc.revision.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.web.doc.Doc;
import org.archboy.clobaframe.web.doc.DocProvider;
import org.archboy.clobaframe.web.doc.revision.RevisionDoc;
import org.archboy.clobaframe.webresource.WebResourceInfo;
import org.archboy.clobaframe.webresource.WebResourceManager;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author yang
 */
@Named
public class WebResourceDocProvider implements DocProvider {

	private static final String DEFAULT_DOC_RESOURCE_PATH = "doc";
	
	@Value("${clobaframe.web.view.docResource.path}")
	private String docResourcePath = DEFAULT_DOC_RESOURCE_PATH;
	
	/**
	 * The doc name example.
	 * terms.md // default English language
	 * terms_ja.md
	 * terms_zh_CN.md
	 * terms_zh_CN.23.md
	 * 
	 * groups:
	 * 1 - name
	 * 3 - lang code
	 * 6 - country code
	 * 8 - revision
	 */
	private static final String docResourceNameRegex = "^([a-zA-Z0-9-]+)(_([a-z]{2})((_)([A-Z]{2}))?)?(.(\\d+))?\\.md$";
	private Pattern docResourceNamePattern = Pattern.compile(docResourceNameRegex);
	
	@Inject
	private WebResourceManager webResourceManager;
	
	private static final Locale defaultLocale = Locale.ENGLISH;
	
	private static final String regexHeader1a = "^(.+)\\n(={3,})(\\n|$)";
	private static final String regexHeader1b = "^(#.+#?)(\\n|$)";
	
	private static final Pattern patternHeader1a = Pattern.compile(regexHeader1a);
	private static final Pattern patternHeader1b = Pattern.compile(regexHeader1b);

	@Override
	public int getPriority() {
		return PRIORITY_NORMAL;
	}
	
	@Override
	public Collection<Doc> getAll() {
		List<Doc> docs = new ArrayList<Doc>();
		
		String basePath = docResourcePath + "/";
		
		// find all script message resource.
		Collection<String> resourceNames = webResourceManager.getAllNames();

		for(String resourceName : resourceNames){

			if (!resourceName.startsWith(basePath)) {
				continue;
			}
			
			String fullname = resourceName.substring(basePath.length());
			String path = null;
			String filename = fullname;
			
			int pos = fullname.lastIndexOf('/');
			if (pos > 0) {
				path = fullname.substring(0, pos);
				filename = fullname.substring(pos + 1);
			}
			
			Matcher matcher = docResourceNamePattern.matcher(filename);
			if (matcher.find()){
				String name = matcher.group(1);
				String langCode = matcher.group(3);
				String countryCode = matcher.group(6);
				String revision = matcher.group(8);

				Locale locale = null;

				if (langCode != null && countryCode != null){
					locale = new Locale(langCode, countryCode);
				}else if (langCode != null){
					locale = new Locale(langCode);
				}else{
					locale = defaultLocale;
				}

				int revisionNumber = 0;
				if (revision != null) {
					revisionNumber = Integer.parseInt(revision);
				}
				
				String parentName = path;
				if (path != null) {
					int lastParentPos = path.lastIndexOf('/');
					if (lastParentPos > 0) {
						parentName = path.substring(lastParentPos + 1);
					}
				}
				
//				Map<Locale, List<RevisionDoc>> localeDocs = docs.get(name);
//				if (localeDocs == null) {
//					localeDocs = new HashMap<Locale, List<RevisionDoc>>();
//					docs.put(name, localeDocs);
//				}
//				
//				List<RevisionDoc> revisionDocs = localeDocs.get(locale);
//				if (revisionDocs == null) {
//					revisionDocs = new ArrayList<RevisionDoc>();
//					localeDocs.put(locale, revisionDocs);
//				}
				
				try{
					WebResourceInfo webResourceInfo = webResourceManager.getResource(resourceName);
					
					RevisionDoc revisionDoc = convertResourceInfo(
								webResourceInfo, 
								name, locale, revisionNumber, 
								parentName);
				
					docs.add(revisionDoc);
				}catch(IOException e){
					// ignore
				}
			}
		}
		
		return docs;
	}
	
	private RevisionDoc convertResourceInfo(WebResourceInfo webResourceInfo, 
			String name, Locale locale, int revision, String parentName) throws IOException{
		
		InputStream in = null;
		try{
			in = webResourceInfo.getContent();
			String content = IOUtils.toString(in, "UTF-8");
			String title = extractTitle(content);
			
			RevisionDoc doc = new RevisionDoc();
			doc.setContent(content);
			doc.setLastModified(webResourceInfo.getLastModified());
			doc.setLocale(locale);
			doc.setName(name);
			doc.setParentName(parentName);
			doc.setRevision(revision);
			doc.setTitle(title);
			
			return doc;
			
		}finally{
			IOUtils.closeQuietly(in);
		}
	}
	
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
