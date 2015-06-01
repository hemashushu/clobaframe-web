package org.archboy.clobaframe.web.controller.form;

import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author yang
 */
public class NoteUpdateForm {
	
	// by using the annontation to add validator
	@NotBlank
	private String title;
	
	private String description;

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
