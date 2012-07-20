package khan.videos.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khan.videos.DAO;
import khan.videos.models.AppUser;
import khan.videos.models.Video;
import khan.videos.servlets.login.BaseUserServlet;
import khan.videos.servlets.templates.Templater;

@SuppressWarnings("serial")
public class MyTribunalServlet extends BaseUserServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp, AppUser user) throws IOException {
		DAO dao = new DAO();
		List<Video> videos = dao.ofy().query(Video.class).filter("user", user).order("submitted").list();
		Templater templater = new Templater();
		templater.put("videos", videos);
		templater.render("tribunal-mine.html", req, resp);
	}

}