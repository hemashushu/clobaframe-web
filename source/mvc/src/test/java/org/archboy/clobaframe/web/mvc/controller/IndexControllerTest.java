package org.archboy.clobaframe.web.mvc.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.archboy.clobaframe.ioc.BeanFactory;
import org.archboy.clobaframe.ioc.impl.DefaultBeanFactory;
import org.archboy.clobaframe.web.mvc.DispatcherServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
/**
 *
 * @author yang
 */
public class IndexControllerTest {
	
	private BeanFactory beanFactory;
	private RestTemplate restTemplate;
	private MockRestServiceServer mockServer;
	private Server server;
	
	@Before
	public void setUp() throws Exception {
		beanFactory = new DefaultBeanFactory(
				"classpath:application.properties",
				"classpath:clobaframe.properties", 
				"classpath:web.properties");
		
		DispatcherServlet dispatcherServlet = beanFactory.get(DispatcherServlet.class);
		
		// build mock object
		restTemplate = new RestTemplate();
		mockServer = MockRestServiceServer.createServer(restTemplate);
		
		// build http server
		server = new Server(18080);
		
		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		server.setHandler(context);
		
		ServletHolder servletHolder = new ServletHolder("dispatcherServlet", dispatcherServlet);
		context.addServlet(servletHolder, "/");
		
		server.start();
	}

	@After
	public void tearDown() throws Exception {
		// stop http server
		server.stop();
	}

	@Test
	public void testIndex() throws Exception {
		
		// test index
		mockServer.expect(requestTo("/"))
				.andRespond(withSuccess("{\"_name\":\"index\",\"hello\":\"world\"}", 
						MediaType.APPLICATION_JSON));
		
		// test return model and view
		mockServer.expect(requestTo("/modelAndView"))
				.andRespond(withSuccess("{\"name\":\"foo\",\"_name\":\"modeAndView\",\"id\":123}", 
						MediaType.APPLICATION_JSON));
		
		// test writer 
		mockServer.expect(requestTo("/writer"))
				.andRespond(withSuccess("hello", 
						MediaType.TEXT_PLAIN));
		
		// test query 
		mockServer.expect(requestTo("/query?id=123&name=foo"))
				.andRespond(withSuccess("{\"name\":\"foo\",\"_name\":\"query\",\"id\":\"123\"}", 
						MediaType.APPLICATION_JSON));
		
		// test form
		mockServer.expect(requestTo("/form"))
				.andRespond(withSuccess("{\"name\":\"bar\",\"_name\":\"form\",\"id\":\"222\"}", 
						MediaType.APPLICATION_JSON));
		
		Map<String, Object> form = new HashMap<>();
		form.put("id", 222);
		form.put("name", "bar");
		
		// test path
		mockServer.expect(requestTo("/path/333"))
				.andRespond(withSuccess("{\"_name\":\"path\",\"id\":\"333\"}", 
						MediaType.APPLICATION_JSON));
		
		// test raw string
		mockServer.expect(requestTo("/string"))
				.andRespond(withSuccess("hello world", 
						MediaType.TEXT_PLAIN));
		
		// test raw object
		mockServer.expect(requestTo("/object"))
				.andRespond(withSuccess("{\"id\":456,\"name\":\"foo\"}", 
						MediaType.APPLICATION_JSON));

		// test request body
		mockServer.expect(requestTo("/requestBody"))
				.andRespond(withSuccess("{\"_name\":\"requestBody\",\"result\":\"success\"}", MediaType.APPLICATION_JSON));
		
		restTemplate.getForObject(new URI("/"), Map.class);
		restTemplate.getForObject(new URI("/modelAndView"), Map.class);
		restTemplate.getForObject(new URI("/writer"), String.class);
		restTemplate.getForObject(new URI("/query?id=123&name=foo"), Map.class);
		restTemplate.postForObject(new URI("/form"), form, Map.class);
		restTemplate.getForObject("/path/{id}", Map.class, "333");
		restTemplate.getForObject("/string", String.class);
		restTemplate.getForObject("/object", Map.class);
		restTemplate.put("/requestBody", form);
		
		// verify all
		mockServer.verify();
	}
	
	@Test
	public void testException() {
		// test exception resolver
		mockServer.expect(requestTo("/exception"))
				.andRespond(withSuccess("{\"error\":{\"code\":\"notFound\"},\"_name\":\"error\"}", MediaType.APPLICATION_JSON));
		
		restTemplate.getForObject("/exception", Map.class);
		
		// verify all
		mockServer.verify();
	}
	
}
