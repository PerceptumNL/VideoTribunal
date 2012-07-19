package khan.videos.servlets.templates;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.asfun.jangod.template.TemplateEngine;

/**
 * Uses the Jangod templating library to render templates.
 */
public class Templater{

	private final static TemplateEngine	engine;
	private final Map<String, Object>	data		= new HashMap<String, Object>();
	private final Map<String, Object>	defaults	= new HashMap<String, Object>();

	static{
		engine = new TemplateEngine();
		engine.getConfiguration().setWorkspace("templates/");
	}

	public void put(String key, Object value){
		data.put(key, value);
	}

	public void render(String templateFile, HttpServletRequest req, HttpServletResponse resp) throws IOException{
		this.defaults.put("session", req.getSession(false));
		this.data.put("defaults", defaults);
		engine.process(templateFile, this.data, resp.getWriter());
	}

}
