package org.archboy.clobaframe.web.view.tool.impl;

import java.util.Locale;
import javax.inject.Named;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author yang
 *
 *  Example uses:
 * $myNumber                            -> 13.55
 * $number.format($myNumber)   -> 13.6
 * $number.currency($myNumber) -> $13.55
 * $number.integer($myNumber)  -> 13
 *
 * See:
 *	http://velocity.apache.org/tools/releases/2.0/javadoc/org/apache/velocity/tools/generic/NumberTool.html
 *  http://velocity.apache.org/tools/releases/2.0/summary.html
 *
 */
@Named
public class LocaleAwareNumberTool extends NumberTool{

	@Override
	public Locale getLocale() {
		return LocaleContextHolder.getLocale();
	}

}
