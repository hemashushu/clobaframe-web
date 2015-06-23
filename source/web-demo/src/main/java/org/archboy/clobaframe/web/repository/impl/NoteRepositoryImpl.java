package org.archboy.clobaframe.web.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.archboy.clobaframe.web.domain.Note;
import org.archboy.clobaframe.web.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

/**
 *
 * @author yang
 */
@Named
public class NoteRepositoryImpl implements NoteRepository {

	@Override
	public Note save(Note note) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Note findOne(String id) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Collection<Note> findAll() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void delete(String id) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

//	@Value("${data.repository.json.path}")
//	private String fileBasePath;
//	
//	@Inject
//	private ResourceLoader resourceLoader;
//	
//	private static final String repoName = "note";
//	
//	private File repoFile;
//	private ObjectMapper objectMapper = new ObjectMapper();
//	private TypeReference<Map<String, Note>> mapperType = new TypeReference<Map<String, Note>>() {};
//	
//	@PostConstruct
//	public void init() throws IOException{
//		Resource resource = resourceLoader.getResource(fileBasePath);
//		File baseDir = resource.getFile();
//		
//		if (!baseDir.exists()){
//			// try to make it
//			baseDir.mkdirs();
//		}
//		
//		repoFile = new File(baseDir, repoName + ".json");
//	}
//	
//	@Override
//	public Note save(Note note) {
//		try{
//			Map<String, Note> notes = null;
//			if (repoFile.exists()) {
//				notes = objectMapper.readValue(repoFile, mapperType);
//			}else{
//				notes = new HashMap<String, Note>();
//			}
//			
//			notes.put(note.getId(), note);
//			objectMapper.writeValue(repoFile, notes);
//			
//		}catch(IOException e){
//			// log it and throw new runtime exception
//		}
//		
//		return note;
//	}
//
//	@Override
//	public Note findOne(String id) {
//		if (!repoFile.exists()) {
//			return null;
//		}
//		
//		try{
//			Map<String, Note> notes = objectMapper.readValue(repoFile, mapperType);
//			return notes.get(id);
//		}catch(IOException e){
//			// log it and throw new runtime exception
//		}
//		
//		return null;
//	}
//
//	@Override
//	public Collection<Note> findAll() {
//		if (!repoFile.exists()) {
//			return new ArrayList<Note>();
//		}
//		
//		try{
//			Map<String, Note> notes = objectMapper.readValue(repoFile, mapperType);
//			return notes.values();
//		}catch(IOException e){
//			// log it
//		}
//		
//		return new ArrayList<Note>();
//	}
//
//	@Override
//	public void delete(String id) {
//		if (!repoFile.exists()) {
//			return;
//		}
//		
//		try{
//			Map<String, Note> notes = objectMapper.readValue(repoFile, mapperType);
//			notes.remove(id);
//			objectMapper.writeValue(repoFile, notes);
//		}catch(IOException e){
//			// log it
//		}
//	}
}
