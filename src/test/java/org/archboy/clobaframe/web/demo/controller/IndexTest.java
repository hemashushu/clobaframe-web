package org.archboy.clobaframe.web.demo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yang
 */
public class IndexTest {
	
	private static final String wwwHost = "http://localhost:8080/";

	protected ObjectMapper mapper = new ObjectMapper();
	
	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testAjax() throws IOException{

		String result1 = get(wwwHost + "ajax");
		
		Map<String, String> map1 = mapper.readValue(
				result1, 
				new TypeReference<Map<String,String>>(){});
		
		assertEquals("002", map1.get("id"));
		assertEquals("bar", map1.get("name"));
		
		String result2 = post(wwwHost + "ajax",
				new BasicNameValuePair("a", "123"),
				new BasicNameValuePair("b", "456"));

		Map<String, Integer> map2 = mapper.readValue(
				result2, 
				new TypeReference<Map<String,Integer>>(){});
			
		
		assertEquals(new Integer(123), map2.get("a"));
		assertEquals(new Integer(456), map2.get("b"));
		assertEquals(new Integer(579), map2.get("value"));
		
	}
	
	private String get(String url, NameValuePair... nameValuePairs) throws IOException {
		RequestBuilder builder = RequestBuilder.get();
		builder.setUri(url);
		builder.addParameters(nameValuePairs);
		HttpUriRequest request = builder.build();
		
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = client.execute(request);

		try {
			return EntityUtils.toString(response.getEntity());
		} finally {
			response.close();
			client.close();
		}
	}
	
	private String post(String url, NameValuePair... nameValuePairs) throws IOException {
		RequestBuilder builder = RequestBuilder.post();
		builder.setUri(url);
		builder.addParameters(nameValuePairs);
		HttpUriRequest request = builder.build();
		
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = client.execute(request);

		try {
			return EntityUtils.toString(response.getEntity());
		} finally {
			response.close();
			client.close();
		}
	}
}
