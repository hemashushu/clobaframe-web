package org.archboy.clobaframe.web.template.velocity;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.web.servlet.View;

/**
 * See {@link org.springframework.web.servlet.view.velocity.VelocityLayoutView}.
 * @author yang
 */
public class VelocityLayoutView implements View {

	private VelocityEngine velocityEngine;

	private String resourceName;
	private String contentType;
	private Map<String, Object> staticAttributes;
	
	// layout support
	private String layoutUrl;
	private String layoutKey;
	private String screenContentKey;

	public VelocityLayoutView(VelocityEngine velocityEngine,
			String resourceName, String contentType, Map<String, Object> staticAttributes, 
			String layoutUrl, String layoutKey, String screenContentKey) {
		this.velocityEngine = velocityEngine;
		this.resourceName = resourceName;
		this.contentType = contentType;
		this.staticAttributes = staticAttributes;
		this.layoutUrl = layoutUrl;
		this.layoutKey = layoutKey;
		this.screenContentKey = screenContentKey;
	}
	
	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, Object> attributes = new HashMap<>();
	
		if (staticAttributes != null && !staticAttributes.isEmpty()) {
			attributes.putAll(staticAttributes);
		}
			
		if (model != null && !model.isEmpty()) {
			attributes.putAll(model);
		}
		
		VelocityContext velocityContext = new VelocityContext(attributes);
		
		response.setContentType(getContentType());
		response.setCharacterEncoding("UTF-8");
		doRender(velocityContext, response.getWriter());
	}
	
	private void doRender(VelocityContext velocityContext, Writer writer) throws Exception {
		renderScreenContent(velocityContext);

		// Velocity context now includes any mappings that were defined
		// (via #set) in screen content template.
		// The screen template can overrule the layout by doing
		// #set( $layout = "MyLayout.vm" )
		String layoutUrlToUse = (String)velocityContext.get(this.layoutKey);
		
		if (layoutUrlToUse == null) {
			// No explicit layout URL given -> use default layout of this view.
			layoutUrlToUse = this.layoutUrl;
		}

		Template layoutTemplate = velocityEngine.getTemplate(layoutUrlToUse);
		layoutTemplate.merge(velocityContext, writer);
	}

	private void renderScreenContent(VelocityContext velocityContext) throws Exception {
		StringWriter sw = new StringWriter();
		Template screenContentTemplate = velocityEngine.getTemplate(resourceName);
		screenContentTemplate.merge(velocityContext, sw);

		// Put rendered content into Velocity context.
		velocityContext.put(this.screenContentKey, sw.toString());
	}
	
}
