package org.archboy.clobaframe.web.service;

import java.io.IOException;
import java.util.Collection;
import org.archboy.clobaframe.io.ResourceInfo;
import org.archboy.clobaframe.web.domain.Note;

/**
 *
 * @author yang
 */
public interface NoteService {
	
	Note create(ResourceInfo resourceInfo, String title, String description) throws IOException;
	
	Note get(String id);
	
	Collection<Note> list();
	
	Note update(String id, String title, String description);
	
	void delete(String id);
	
	ResourceInfo getPhoto(String photoId);
	
}
