package org.archboy.clobaframe.web.page;

import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * @author yang
 */
public class Page {
	
	private PageKey pageKey;
	
	/**
	 * URL name will override the default page URL that combined by page name and parent page names.
	 * Only a-z, A-Z, 0-9 and '-' and '/' are allowed in this property value.
	 * Optional.
	 */
	private String urlName;
	
	/**
	 * The name of template that used for render this doc.
	 * Optional.
	 */
	private String templateName;
	
	private String title;
	private String content;
	private Date lastModified;

	/**
	 * The update note.
	 * Optional.
	 */
	private String updateNote;
	
	/**
	 * The author name and id are optional.
	 */
	private String authorName;
	private String authorId;

	public PageKey getPageKey() {
		return pageKey;
	}

	public void setPageKey(PageKey pageKey) {
		this.pageKey = pageKey;
	}
	
	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
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

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getUpdateNote() {
		return updateNote;
	}

	public void setUpdateNote(String updateNote) {
		this.updateNote = updateNote;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(getPageKey())
				.toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null){
			return false;
		}

		if (o == this){
			return true;
		}

		if(o.getClass() != getClass()){
			return false;
		}

		Page other = (Page)o;
		return new EqualsBuilder()
				.append(getPageKey(), other.getPageKey())
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("pageKey", getPageKey())
				.append("title", getTitle())
				.toString();
	}
}
