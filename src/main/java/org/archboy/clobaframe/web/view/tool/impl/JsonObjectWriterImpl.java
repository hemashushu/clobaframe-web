package org.archboy.clobaframe.web.view.tool.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.archboy.clobaframe.web.view.tool.JsonObjectWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.inject.Named;

/**
 *
 * @author yang
 */
@Named
public class JsonObjectWriterImpl implements JsonObjectWriter{

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public String write(Object obj) {
		try{
			return mapper.writeValueAsString(obj);
		}catch(JsonProcessingException e){
			return null;
		}
	}
}
