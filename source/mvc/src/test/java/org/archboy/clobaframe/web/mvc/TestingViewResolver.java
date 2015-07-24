package org.archboy.clobaframe.web.mvc;

import java.util.Locale;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 *
 * @author yang
 */
public class TestingViewResolver implements ViewResolver {

	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		return new TestingView(viewName);
	}
	
}
