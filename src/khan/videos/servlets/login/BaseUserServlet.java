package khan.videos.servlets.login;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import khan.videos.models.AppUser;

/**
 * Base servlet for servlets that require a user to be logged in.
 */
@SuppressWarnings("serial")
public class BaseUserServlet extends HttpServlet {

	@Override
	public final void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession session = req.getSession();
		if (session != null) {
			AppUser user = (AppUser) session.getAttribute("AppUser");
			if (user != null) {
				this.doGet(req, resp, user);
				return;
			}
		}
		resp.sendRedirect(BaseUserServlet.createLoginURL(req));
	}

	@Override
	public final void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession session = req.getSession();
		if (session != null) {
			AppUser user = (AppUser) session.getAttribute("AppUser");
			if (user != null) {
				this.doPost(req, resp, user);
				return;
			}
		}
		resp.sendRedirect(BaseUserServlet.createLoginURL(req));
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			AppUser user) throws IOException {
		// To be implemented by subclasses.
		resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		resp.getOutputStream().print("GET not implemented.");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp,
			AppUser user) throws IOException {
		// To be implemented by subclasses.
		resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		resp.getOutputStream().print("POST not implemented.");
	}

	/**
	 * Creates login URL, user will be redirected to request upon login
	 * complete.
	 */
	public static String createLoginURL(HttpServletRequest req)
			throws IOException {
		StringBuffer url = req.getRequestURL();
		String queryString = req.getQueryString();
		if (queryString != null) {
			url.append('?');
			url.append(queryString);
		}
		return "/login?page=" + URLEncoder.encode(url.toString(), "UTF-8");
	}

}
