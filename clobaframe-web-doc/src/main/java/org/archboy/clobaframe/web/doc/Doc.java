package org.archboy.clobaframe.web.doc;

import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * @author yang
 */
public class Doc {
	
	/**
	 * The value of combine name and locale is the Doc id.
	 * Only a-z, A-Z, 0-9 and '-' are allowed in the name.
	 */
	private String name;
	private Locale locale;
	
	/**
	 * The name of parent doc.
	 * NULL for the top most level doc.
	 */
	private String parentName;
	
	private String title;
	private String content;
	private Date lastModified;
	
	/**
	 * The author properties are optional.
	 */
	private String authorName;
	private String authorId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
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

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(getName())
				.append(getLocale())
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

		Doc other = (Doc)o;
		return new EqualsBuilder()
				.append(getName(), other.getName())
				.append(getLocale(), other.getLocale())
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("name", getName())
				.append("locale", getLocale())
				.append("title", getTitle())
				.toString();
	}
}
