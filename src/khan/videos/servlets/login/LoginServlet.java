package khan.videos.servlets.login;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import khan.videos.DAO;
import khan.videos.models.AppUser;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Login via OpenId
 */
@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		if (userService.isUserLoggedIn()) {
			DAO dao = DAO.get();
			AppUser appUser = dao.loginAppUser(userService.getCurrentUser(), userService.isUserAdmin());
			HttpSession session = req.getSession(true);
			session.setAttribute("AppUser", appUser);
			session.setAttribute("admin", appUser.getRank() == AppUser.Rank.Administrator);
			String page = req.getParameter("page");
			if (page == null)
				page = "/";
			resp.sendRedirect(page);
		} else {
			StringBuffer url = req.getRequestURL();
			String queryString = req.getQueryString();
			if (queryString != null) {
				url.append('?');
				url.append(queryString);
			}
			resp.sendRedirect(userService.createLoginURL(url.toString(), null, "gmail.com", null));
		}
	}

}
