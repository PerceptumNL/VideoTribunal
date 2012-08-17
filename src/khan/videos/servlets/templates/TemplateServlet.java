package khan.videos.servlets.templates;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khan.videos.servlets.login.BaseUserServlet;

@SuppressWarnings("serial")
public class TemplateServlet extends HttpServlet {

	public static final Map<String, String> guestTemplates = new HashMap<String, String>();
	public static final Map<String, String> userTemplates = new HashMap<String, String>();

	{
		// Templates for guests only
		guestTemplates.put("/", "home.html");
		guestTemplates.put("/home", "home.html");
		// Templates logged in users can view
		userTemplates.putAll(guestTemplates);
		userTemplates.put("/overview", "overview-khan.html");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Templater templater = new Templater();
		String uri = req.getRequestURI();
		if (req.getSession(false) == null) {
			// User not logged in
			String template = guestTemplates.get(uri);
			if (template == null) {
				// Check if template is in userTemplates - if so redirect to
				// login page.
				if (userTemplates.get(uri) == null)
					resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				else {
					resp.sendRedirect(BaseUserServlet.createLoginURL(req));
				}
			} else {
				templater.render(template, req, resp);
			}
		} else {
			// User logged in
			String template = userTemplates.get(uri);
			if (template == null) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			} else {
				templater.render(template, req, resp);
			}
		}
	}

}
