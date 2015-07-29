package org.archboy.clobaframe.web.demo.tool;

import java.util.Locale;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.web.page.PageInfo;
import org.archboy.clobaframe.web.page.revision.RevisionPageInfo;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author yang
 */
@Named
public class ObjectUrl {
	
	public String page(PageInfo pageInfo) {
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		if (StringUtils.isEmpty(pageInfo.getUrlName())){
			builder.path("/page/" + pageInfo.getPageKey().getName());
		}else{
			builder.path("/" + pageInfo.getUrlName());
		}
		
		return builder.toUriString();
	}
	
	public String page(PageInfo pageInfo, Locale locale) {
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		if (StringUtils.isEmpty(pageInfo.getUrlName())){
			builder.path("/page/" + pageInfo.getPageKey().getName());
		}else{
			builder.path("/" + pageInfo.getUrlName());
		}
		
		builder.queryParam("locale", locale);
		
		return builder.toUriString();
	}
	
	public String revisionPage(RevisionPageInfo pageInfo) {
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		if (StringUtils.isEmpty(pageInfo.getUrlName())){
			builder.path("/page/" + pageInfo.getPageKey().getName());
		}else{
			builder.path("/" + pageInfo.getUrlName());
		}
		
		builder.queryParam("locale", pageInfo.getPageKey().getLocale());
		builder.queryParam("revision", pageInfo.getRevision());
		
		return builder.toUriString();
	}
}
