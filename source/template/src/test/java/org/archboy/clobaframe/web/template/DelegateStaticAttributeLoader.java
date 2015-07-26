package org.archboy.clobaframe.web.template;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.archboy.clobaframe.ioc.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * @author yang
 */
public class DelegateStaticAttributeLoader implements StaticAttributeLoader {
	
	public static final String SETTING_KEY_STATIC_ATTRIBUTES_DEFINE_FILE_NAME = "clobaframe.web.template.staticAttributeDefineFileName";
	public static final String DEFAULT_STATIC_ATTRIBUTES_DEFINE_FILE_NAME = ""; //"classpath:staticViewAttribute.json";
	
	@Value("${" + SETTING_KEY_STATIC_ATTRIBUTES_DEFINE_FILE_NAME + ":" + DEFAULT_STATIC_ATTRIBUTES_DEFINE_FILE_NAME + "}")
	private String staticAttributesDefineFileName; // = DEFAULT_STATIC_ATTRIBUTES_DEFINE_FILE_NAME;

	@Inject
	private ResourceLoader resourceLoader;
	
	@Inject
	private BeanFactory beanFactory;

	private ObjectMapper objectMapper = new ObjectMapper();
	private Map<String, Object> staticAttributes = new HashMap<>();

	public void setStaticAttributesDefineFileName(String staticAttributesDefineFileName) {
		this.staticAttributesDefineFileName = staticAttributesDefineFileName;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	@PostConstruct
	public void init() throws Exception {
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
			TypeReference<Map<String, String>> typeReference = new TypeReference<Map<String, String>>() {};
			Map<String, String> map = objectMapper.readValue(in, typeReference);
			
			for(Map.Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String beanId = entry.getValue();
				Object bean = beanFactory.get(beanId);
				
				if (bean == null) {
					throw new IllegalArgumentException(
							String.format("Can not found the bean [id=%s]", beanId));
				}
				
				staticAttributes.put(key, bean);
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
