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
import org.archboy.clobaframe.blobstore.BlobResourceInfo;
import org.archboy.clobaframe.blobstore.BlobResourceInfoFactory;
import org.archboy.clobaframe.blobstore.BlobResourceRepository;
import org.archboy.clobaframe.blobstore.Blobstore;
import org.archboy.clobaframe.blobstore.BlobstoreManager;
import org.archboy.clobaframe.blobstore.impl.DefaultBlobResourceInfoFactory;
import org.archboy.clobaframe.blobstore.local.LocalBlobstore;
import org.archboy.clobaframe.io.ResourceInfo;
import org.archboy.clobaframe.query.simplequery.SimpleQuery;
import org.archboy.clobaframe.web.domain.UserContent;
import org.archboy.clobaframe.web.service.UserContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yang
 */
@Named
public class UserContentServiceImpl implements UserContentService {

	@Inject
	private BlobstoreManager blobstoreManager;
	
//	@Inject
//	@Named("defaultBlobstore")
//	private Blobstore blobstore;
	
	private BlobResourceRepository blobResourceRepository;
	
	private Map<String, UserContent> userContents = new HashMap<String, UserContent>();
	
	private final static String blobRepoName = "clobaframe-web-demo";
	
	private final Logger logger = LoggerFactory.getLogger(UserContentServiceImpl.class);
	
	@PostConstruct
	public void init() throws IOException{
		Blobstore blobstore = blobstoreManager.getDefault();
		
		if (!blobstore.exist(blobRepoName)) {
			blobstore.create(blobRepoName);
		}
		
		blobResourceRepository = blobstore.getRepository(blobRepoName);
	}
	
	@Override
	public UserContent create(ResourceInfo resourceInfo, String title, String description) throws IOException {
		String id = UUID.randomUUID().toString();
		BlobResourceInfoFactory factory = new DefaultBlobResourceInfoFactory();
		BlobResourceInfo blobResourceInfo = factory.make(blobRepoName, id, resourceInfo, null);
		blobResourceRepository.put(blobResourceInfo);
		
		UserContent userContent = new UserContent();
		userContent.setContentLength(resourceInfo.getContentLength());
		userContent.setCreationTime(resourceInfo.getLastModified());
		userContent.setId(id);
		userContent.setMimeType(resourceInfo.getMimeType());
		userContent.setTitle(title);
		userContent.setDescription(description);
		
		userContents.put(id, userContent);
		
		return userContent;
	}

	@Override
	public UserContent get(String id) {
		return userContents.get(id);
	}

	@Override
	public ResourceInfo getData(String id) {
		return blobResourceRepository.get(id);
	}
	
	@Override
	public void delete(String id) {
		try{
			blobResourceRepository.delete(id);
		}catch(IOException e){
			logger.warn("Delete blob resource %s failed, message: %s.", id, e.getMessage());
		}
		
		
		userContents.remove(id);
	}

	@Override
	public Collection<UserContent> list() {
		return userContents.values();
	}
	
}
