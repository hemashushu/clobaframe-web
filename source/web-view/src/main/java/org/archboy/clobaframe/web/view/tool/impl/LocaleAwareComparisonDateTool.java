package org.archboy.clobaframe.web.view.tool.impl;

import java.util.Locale;
import javax.inject.Named;
import org.apache.velocity.tools.generic.ComparisonDateTool;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author yang
 *
 * Example of formatting the "current" date:
 *   $date                         -> Oct 19, 2003 9:54:50 PM
 *   $date.long                    -> October 19, 2003 9:54:50 PM PDT
 *   $date.medium_time             -> 9:54:50 PM
 *   $date.full_date               -> Sunday, October 19, 2003
 *   $date.get('default','short')  -> Oct 19, 2003 9:54 PM
 *   $date.get('yyyy-M-d H:m:s')   -> 2003-10-19 21:54:50
 *
 * Example of formatting an arbitrary date:
 *   $myDate                        -> Tue Oct 07 03:14:50 PDT 2003
 *   $date.format('medium',$myDate) -> Oct 7, 2003 3:14:50 AM
 *
 * Example of formatting the "current" date:
 * $date.whenIs('2005-07-04')                -> 1 year ago
 *   $date.whenIs('2007-02-15').full           -> 1 year 32 weeks 2 days 17 hours 38 minutes 44 seconds 178 milliseconds ago
 *   $date.whenIs('2007-02-15').days           -> -730
 *   $date.whenIs($date.calendar)              -> now
 *   $date.whenIs('2005-07-04', '2005-07-04')  -> same time
 *   $date.difference('2005-07-04','2005-07-04')      -> 0 milliseconds
 *   $date.difference('2005-07-04','2007-02-15').abbr -> 1 yr
 *
 * See:
 *	http://velocity.apache.org/tools/releases/2.0/javadoc/org/apache/velocity/tools/generic/DateTool.html
 *	http://velocity.apache.org/tools/releases/2.0/javadoc/org/apache/velocity/tools/generic/ComparisonDateTool.html
 *	http://velocity.apache.org/tools/releases/2.0/summary.html
 *
 */
@Named
public class LocaleAwareComparisonDateTool extends ComparisonDateTool {

	@Override
	public Locale getLocale() {
		return LocaleContextHolder.getLocale();
	}
}
