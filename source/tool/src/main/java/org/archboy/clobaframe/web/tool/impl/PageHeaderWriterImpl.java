package org.archboy.clobaframe.web.tool.impl;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.web.tool.PageHeaderWriter;
import org.archboy.clobaframe.web.tool.PageHeaderProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * 
 * @author yang
 */
@Named
public class PageHeaderWriterImpl implements PageHeaderWriter {

	@Autowired(required = false)
	private List<PageHeaderProvider> pageHeaderProviders; // = new ArrayList<PageHeaderProvider>();
	
	public void setPageHeaderProviders(List<PageHeaderProvider> pageHeaderProviders) {
		this.pageHeaderProviders = pageHeaderProviders;
	}

	@Override
	public void addProvider(PageHeaderProvider pageHeaderProvider) {
		if (pageHeaderProviders == null) {
			pageHeaderProviders = new ArrayList<>();
		}
		
		pageHeaderProviders.add(pageHeaderProvider);
	}

	@Override
	public void removeProvider(String providerName) {
		Assert.notNull(providerName);
		
		for (int idx = pageHeaderProviders.size() - 1; idx >= 0; idx--){
			PageHeaderProvider provider = pageHeaderProviders.get(idx);
			if (providerName.equals(provider.getName())){
				pageHeaderProviders.remove(idx);
				break;
			}
		}
	}

	@Override
	public String write() {
		return write(null);
	}

	@Override
	public String write(String seperator) {
		List<String> headers = new ArrayList<String>();
		
		if (pageHeaderProviders != null && !pageHeaderProviders.isEmpty()) {
			for(PageHeaderProvider pageHeaderProvider : pageHeaderProviders){
				headers.addAll(pageHeaderProvider.list());
			}
		}
		
		if (headers.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		return StringUtils.join(headers, seperator);
	}
}