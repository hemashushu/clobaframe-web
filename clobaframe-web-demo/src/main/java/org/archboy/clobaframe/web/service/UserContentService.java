package org.archboy.clobaframe.web.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.archboy.clobaframe.io.ResourceInfo;
import org.archboy.clobaframe.web.domain.UserContent;

/**
 *
 * @author yang
 */
public interface UserContentService {
	
	UserContent create(ResourceInfo resourceInfo, String title, String description) throws IOException;
	
	UserContent get(String id);
	
	ResourceInfo getData(String id);
	
	void delete(String id);
	
	Collection<UserContent> list();
	
}
