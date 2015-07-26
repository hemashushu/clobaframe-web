package org.archboy.clobaframe.web.template;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * @author yang
 */
public class SpringStaticAttributeLoader implements StaticAttributeLoader, ResourceLoaderAware, BeanFactoryAware, InitializingBean {
	
	public static final String SETTING_KEY_STATIC_ATTRIBUTES_DEFINE_FILE_NAME = "clobaframe.web.template.staticAttributeDefineFileName";
	public static final String DEFAULT_STATIC_ATTRIBUTES_DEFINE_FILE_NAME = ""; //"classpath:staticViewAttribute.json";
	
	@Value("${" + SETTING_KEY_STATIC_ATTRIBUTES_DEFINE_FILE_NAME + ":" + DEFAULT_STATIC_ATTRIBUTES_DEFINE_FILE_NAME + "}")
	private String staticAttributesDefineFileName; // = DEFAULT_STATIC_ATTRIBUTES_DEFINE_FILE_NAME;

	private ResourceLoader resourceLoader;
	
	private BeanFactory beanFactory;

	private ObjectMapper objectMapper = new ObjectMapper();
	private Map<String, Object> staticAttributes = new HashMap<>();
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void setStaticAttributesDefineFileName(String staticAttributesDefineFileName) {
		this.staticAttributesDefineFileName = staticAttributesDefineFileName;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//@PostConstruct
		//public void init() throws Exception {
		
		if (StringUtils.isEmpty(staticAttributesDefineFileName)) {
			return;
		}
		
		Resource resource = resourceLoader.getResource(staticAttributesDefineFileName);
		if (!resource.exists()) {
			throw new FileNotFoundException(
					String.format("Can not found the view static attribute define file [%s].", 
							staticAttributesDefineFileName));
		}
		
		InputStream in = null;
		try{
			in = resource.getInputStream();
			TypeReference<Collection<Map<String, String>>> typeReference = 
					new TypeReference<Collection<Map<String, String>>>() {};
			
			Collection<Map<String, String>> items = objectMapper.readValue(in, typeReference);
			
			for(Map<String, String> item : items){
				String key = item.get("key");
				String beanId = item.get("bean");
				Object object = beanFactory.getBean(beanId);

				if (object == null) {
					throw new IllegalArgumentException(
							String.format("Can not found the bean [id=%s]", beanId));
				}

				staticAttributes.put(key, object);
			}
		}finally{
			IOUtils.closeQuietly(in);
		}
	}
	
	@Override
	public Map<String, Object> list() {
		return staticAttributes;
	}

}
