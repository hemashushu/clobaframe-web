package org.archboy.clobaframe.web.view.tool.impl;

import org.archboy.clobaframe.web.view.tool.MessageSourceTool;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author yang
 */
@Named
public class MessageSourceToolImpl implements MessageSourceTool {

	@Inject
	private MessageSource messageSource;

	@Override
	public String write(String code, Object... args){
		return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
	}

	@Override
	public String write(String code, List<Object> args){
		return messageSource.getMessage(code, args.toArray(), LocaleContextHolder.getLocale());
	}
	
	@Override
	public String getLocale() {
		Locale locale = LocaleContextHolder.getLocale();
		return locale.toLanguageTag();
	}
}
