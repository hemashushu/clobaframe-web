package org.archboy.clobaframe.web.demo.controller.form;

import java.util.Locale;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author yang
 */
public class PagePostForm {

	@NotNull
	private Locale locale;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String title;
	
	@NotBlank
	private String content;
	
	@Pattern.List({
		@Pattern(regexp = "^$"),
		@Pattern(regexp = "^$|^[a-zA-Z0-9\\.\\-\\/]+$")
	})
	private String urlName;

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

}
