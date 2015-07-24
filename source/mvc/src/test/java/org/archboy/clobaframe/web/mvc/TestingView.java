package org.archboy.clobaframe.web.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.View;

/**
 *
 * @author yang
 */
public class TestingView implements View {

	private String name;
	private ObjectMapper objectMapper = new ObjectMapper();

	public TestingView(String name) {
		this.name = name;
	}
	
	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<>();
		if (model != null && !model.isEmpty()){
			map.putAll(model);
		}
		
		map.put("_name", name);
		response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
		objectMapper.writeValue(response.getOutputStream(), map);
	}
}
