package org.archboy.clobaframe.web.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.blobstore.BlobResourceInfo;
import org.archboy.clobaframe.blobstore.BlobResourceInfoFactory;
import org.archboy.clobaframe.blobstore.BlobResourceRepository;
import org.archboy.clobaframe.blobstore.Blobstore;
import org.archboy.clobaframe.blobstore.BlobstoreManager;
import org.archboy.clobaframe.blobstore.impl.DefaultBlobResourceInfoFactory;
import org.archboy.clobaframe.blobstore.local.LocalBlobstore;
import org.archboy.clobaframe.io.ResourceInfo;
import org.archboy.clobaframe.query.simplequery.SimpleQuery;
import org.archboy.clobaframe.web.domain.Note;
import org.archboy.clobaframe.web.exception.NotFoundException;
import org.archboy.clobaframe.web.repository.NoteRepository;
import org.archboy.clobaframe.web.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yang
 */
@Named
public class NoteServiceImpl implements NoteService {

	@Inject
	private BlobstoreManager blobstoreManager;
	
	private BlobResourceRepository blobResourceRepository;
	
	@Inject
	private NoteRepository noteRepository;
	
	private final static String blobRepoName = "clobaframe-web-demo";
	
	private final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);
	
	@PostConstruct
	public void init() throws IOException{
		Blobstore blobstore = blobstoreManager.getDefault();
		
		if (!blobstore.exist(blobRepoName)) {
			blobstore.create(blobRepoName);
		}
		
		blobResourceRepository = blobstore.getRepository(blobRepoName);
	}
	
	@Override
	public Note create(ResourceInfo resourceInfo, String title, String description) throws IOException {
		String photoId = UUID.randomUUID().toString();
		BlobResourceInfoFactory factory = new DefaultBlobResourceInfoFactory();
		BlobResourceInfo blobResourceInfo = factory.make(blobRepoName, photoId, resourceInfo, null);
		blobResourceRepository.put(blobResourceInfo);
		
		String id = UUID.randomUUID().toString();
		Note note = new Note();
		note.setCreationTime(resourceInfo.getLastModified());
		note.setDescription(description);
		note.setId(id);
		note.setPhotoId(photoId);
		note.setTitle(title);
		
		noteRepository.save(note);
		
		return note;
	}

	@Override
	public Note get(String id) {
		return noteRepository.findOne(id);
	}

	@Override
	public Collection<Note> list() {
		return noteRepository.findAll();
	}

	@Override
	public Note update(String id, String title, String description) {
		Note note = noteRepository.findOne(id);
		if (note == null) {
			throw new NotFoundException();
		}
		
		Note n = new Note();
		n.setCreationTime(note.getCreationTime());
		n.setDescription(description);
		n.setId(note.getId());
		n.setPhotoId(note.getPhotoId());
		n.setTitle(title);
		
		noteRepository.save(n);
		return n;
	}
	
	@Override
	public void delete(String id) {
		Note note = noteRepository.findOne(id);
		if (note == null) {
			return;
		}
		
		try{
			blobResourceRepository.delete(note.getPhotoId());
		}catch(IOException e){
			logger.warn("Delete blob resource %s failed, message: %s.", id, e.getMessage());
		}
		
		
		noteRepository.delete(id);
	}

	@Override
	public ResourceInfo getPhoto(String photoId) {
		ResourceInfo resourceInfo = blobResourceRepository.get(photoId);
		if (resourceInfo == null) {
			throw new NotFoundException();
		}
		
		return resourceInfo;
	}
	
}
