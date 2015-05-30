package org.archboy.clobaframe.web.page;

import java.util.Locale;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * @author yang
 */
public class PageKey {
	
		/**
	 * Page name that includes path.
	 * The default page URL is /page/[path-name1]/[path-name2]/[path-nameX]/name.
	 * This value combines the locale act as page id.
	 * Only a-z, A-Z, 0-9 and '-' and '/' are allowed.
	 */
	private String name;
	
	/**
	 * The default locale is 'en'.
	 */
	private Locale locale;

	public PageKey() {
	}

	public PageKey(String name, Locale locale) {
		this.name = name;
		this.locale = locale;
	}

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

		PageKey other = (PageKey)o;
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
				.toString();
	}
}
