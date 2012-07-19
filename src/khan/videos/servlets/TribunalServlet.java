package khan.videos.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khan.videos.DAO;
import khan.videos.models.Video;
import khan.videos.servlets.templates.Templater;

@SuppressWarnings("serial")
public class TribunalServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		DAO dao = new DAO();
		List<Video> videos = dao.ofy().query(Video.class).order("submitted").list();
		Templater templater = new Templater();
		if (req.getParameter("message") != null) {
			templater.put("message", req.getParameter("message"));
		}
		templater.put("videos", videos);
		templater.render("tribunal.html", req, resp);
	}

}
