package org.archboy.clobaframe.web.tool.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.archboy.clobaframe.web.tool.JsonWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yang
 */
@Named("jsonWriter")
public class JsonWriterImpl implements JsonWriter{

	private ObjectMapper mapper = new ObjectMapper();

	private final Logger logger = LoggerFactory.getLogger(JsonWriterImpl.class);
	
	@Override
	public String write(Object obj) {
		if (obj == null) {
			return StringUtils.EMPTY;
		}
		
		try{
			return mapper.writeValueAsString(obj);
		}catch(JsonProcessingException e){
			logger.error("Write JSON object error.", e);
			return StringUtils.EMPTY;
		}
	}
}
