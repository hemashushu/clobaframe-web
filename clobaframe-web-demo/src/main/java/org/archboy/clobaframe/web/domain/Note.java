package org.archboy.clobaframe.web.domain;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author yang
 */
public class Note implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private Date creationTime;
	private String title;
	private String description;
	private String photoId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
