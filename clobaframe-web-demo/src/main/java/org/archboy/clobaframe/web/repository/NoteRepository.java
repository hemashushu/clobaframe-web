package org.archboy.clobaframe.web.repository;

import java.util.Collection;
import org.archboy.clobaframe.web.domain.Note;

/**
 *
 * @author yang
 */
public interface NoteRepository {
	
	Note save(Note note);
	
	Note findOne(String id);
	
	Collection<Note> findAll();
	
	void delete(String id);
}
