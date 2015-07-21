package org.archboy.clobaframe.web.page.revision;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.archboy.clobaframe.web.page.PageInfo;

/**
 *
 * @author yang
 */
public class RevisionPageInfo extends PageInfo {
	
	/**
	 * The page revision number.
	 * The default page (that the first create) revision number is 0.
	 */
	private int revision;
	
	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(getPageKey())
				.append(getRevision())
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

		RevisionPageInfo other = (RevisionPageInfo)o;
		return new EqualsBuilder()
				.append(getPageKey(), other.getPageKey())
				.append(getRevision(), other.getRevision())
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("pageKey", getPageKey())
				.append("revision", getRevision())
				.append("title", getTitle())
				.toString();
	}
}
