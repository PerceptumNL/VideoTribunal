package khan.videos.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khan.videos.DAO;
import khan.videos.models.Video;
import khan.videos.models.Video.Status;
import khan.videos.servlets.templates.Templater;

@SuppressWarnings("serial")
public class TribunalServlet extends HttpServlet {

	public static final String requestTribunalVoting = "/tribunal";
	public static final String requestTribunalAccepted = "/accepted";

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		DAO dao = DAO.get();
		String requestTribunal = req.getServletPath();
		List<Video> videos = null;
		if (requestTribunalVoting.equals(requestTribunal)) {
			videos = dao.ofy().query(Video.class).filter("status", Status.Voting).order("submitted").list();
		} else if (requestTribunalAccepted.equals(requestTribunal)) {
			videos = dao.ofy().query(Video.class).filter("status", Status.Accepted).order("submitted").list();
		}
		Templater templater = new Templater();
		templater.put("videos", videos);
		templater.render("tribunal.html", req, resp);
	}

}
