package khan.videos.servlets.login;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Redirects to the logout URL
 */
@SuppressWarnings("serial")
public class LogoutServlet extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		UserService userService = UserServiceFactory.getUserService();
		String logoutURL = userService.createLogoutURL("/");
		HttpSession session = req.getSession();
		if(session != null){
			session.invalidate();
		}
		resp.sendRedirect(logoutURL);
	}

}
