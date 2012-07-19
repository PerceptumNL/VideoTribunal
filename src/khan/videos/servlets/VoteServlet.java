package khan.videos.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khan.videos.DAO;
import khan.videos.models.AppUser;
import khan.videos.models.Video;
import khan.videos.models.Vote;
import khan.videos.servlets.login.BaseUserServlet;
import khan.videos.servlets.templates.Templater;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;

@SuppressWarnings("serial")
public class VoteServlet extends BaseUserServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp, AppUser user) throws IOException {
		// Get valid Id
		String id = req.getParameter("id");
		if (id == null || id.length() != 11) {
			resp.sendRedirect("/tribunal");
			return;
		}
		// Load video
		try {
			DAO dao = new DAO();
			Video video = dao.ofy().get(new Key<Video>(Video.class, id));
			List<Vote> votes = dao.ofy().query(Vote.class).filter("video", new Key<Video>(Video.class, id)).list();
			Templater templater = new Templater();
			templater.put("video", video);
			templater.put("votes", votes);
			templater.render("vote.html", req, resp);
		} catch (NotFoundException e) {
			resp.sendRedirect("/tribunal");
		}
	}

	// TODO: Topics, Exercise List
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp, AppUser user) throws IOException {
		String youtubeId = req.getParameter("youtubeId");
		String status = req.getParameter("status");
		String comment = req.getParameter("comment");
		try {
			if ("accepted".equals(status) || "denied".equals(status)) {
				Boolean accepted = "accepted".equals(status);
				DAO dao = new DAO();
				Video video = dao.ofy().get(Video.class, youtubeId);
				dao.ofy().put(new Vote(user, video, "Topicless", new ArrayList<String>(), accepted, comment));
				resp.sendRedirect("/tribunal?message=1");
			} else {
				resp.sendRedirect("/vote?id=" + youtubeId);
			}
		} catch (NotFoundException e) {
			resp.sendRedirect("/tribunal");
		}
	}

}