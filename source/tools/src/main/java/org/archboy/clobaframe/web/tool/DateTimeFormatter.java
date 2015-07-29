package org.archboy.clobaframe.web.tool;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import javax.inject.Named;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author yang
 */
@Named("dateTimeFormatter")
public class DateTimeFormatter {
	
	public String date(Date date, String style) {
		return date(date, style, LocaleContextHolder.getLocale());
	}
	
	public String date(Date date, String style, Locale locale) {
		DateFormat dateFormat = DateFormat.getDateInstance(convertStyle(style), locale);
		return dateFormat.format(date);
	}

	public String time(Date date, String style) {
		return time(date, style, LocaleContextHolder.getLocale());
	}
	
	public String time(Date date, String style, Locale locale) {
		DateFormat dateFormat = DateFormat.getTimeInstance(convertStyle(style), locale);
		return dateFormat.format(date);
	}
	
	public String datetime(Date date, String dateStyle, String timeStyle) {
		return datetime(date, dateStyle, timeStyle, LocaleContextHolder.getLocale());
	}
	
	public String datetime(Date date, String dateStyle, String timeStyle, Locale locale) {
		DateFormat dateFormat = DateFormat.getDateTimeInstance(
				convertStyle(dateStyle), convertStyle(timeStyle), locale);
		return dateFormat.format(date);
	}
	
	
	private int convertStyle(String style) {
		int result = 0;
		switch(style){
			case "full":
				result = DateFormat.FULL;
				break;
			case "long":
				result = DateFormat.LONG;
				break;
			case "medium":
				result = DateFormat.MEDIUM;
				break;
			case "short":
				result = DateFormat.SHORT;
				break;
		}
		return result;
	}
	
}
