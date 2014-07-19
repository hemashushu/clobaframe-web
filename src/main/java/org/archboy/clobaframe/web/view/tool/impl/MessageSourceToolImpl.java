package org.archboy.clobaframe.web.view.tool.impl;

import org.archboy.clobaframe.web.view.tool.MessageSourceTool;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * The i18n message source tool for view.
 *
 * @author yang
 */
@Named
public class MessageSourceToolImpl implements MessageSourceTool {

	@Inject
	private MessageSource messageSource;

	@Override
	public String get(String code, Object... args){
		return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
	}

	@Override
	public String get(String code, List<Object> args){
		return messageSource.getMessage(code, args.toArray(), LocaleContextHolder.getLocale());
	}
}
