package org.archboy.clobaframe.web.template.velocity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.Log4JLogChute;
import org.archboy.clobaframe.web.template.ViewResourceManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

/**
 * See {@link VelocityConfigurer} and {@link VelocityEngineFactory}.
 * 
 * @author yang
 */
@Named
public class VelocityEngineFactory {
	
	@Inject
	private ResourceLoader resourceLoader;
	
	@Inject
	private ViewResourceManager viewResourceManager;
	
	private static final String SETTING_KEY_CONFIG_FILE_NAME = "clobaframe.web.template.velocity.configFileName";
	private static final String DEFAULT_CONFIG_FILE_NAME = ""; // "classpath:velocity.properties"
	
	@Value("${" + SETTING_KEY_CONFIG_FILE_NAME + ":" + DEFAULT_CONFIG_FILE_NAME + "}")
	private String configFileName;
	
	private VelocityEngine velocityEngine;
	
	@PostConstruct
	public void init() throws Exception {
		Properties properties = new Properties();
		
		if (StringUtils.isNotEmpty(configFileName)) {
			Resource resource = resourceLoader.getResource(configFileName);
			
			if (!resource.exists()) {
				throw new FileNotFoundException(String.format(
						"Can not find the velocity config file [%s].",
						configFileName));
			}

			InputStream in = null;
			try{
				in = resource.getInputStream();
				properties.load(in);
			}finally{
				IOUtils.closeQuietly(in);
			}
		}
		
		VelocityEngine engine = new VelocityEngine(properties);
		engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, new Log4JLogChute()); // set using Log4J
		initResourceLoader(engine);
		engine.init();
		
		this.velocityEngine = engine;
	}
	
	protected void initResourceLoader(VelocityEngine velocityEngine) {
		velocityEngine.setProperty(
				RuntimeConstants.RESOURCE_LOADER, DelegateVelocityViewResourceLoader.NAME);
		velocityEngine.setProperty(
				DelegateVelocityViewResourceLoader.RESOURCE_LOADER_CLASS, DelegateVelocityViewResourceLoader.class.getName());
		velocityEngine.setProperty(
				DelegateVelocityViewResourceLoader.VIEW_RESOURCE_MANAGER, viewResourceManager);
	}
	
	public VelocityEngine getVelocityEngine(){
		return velocityEngine;
	}
}
