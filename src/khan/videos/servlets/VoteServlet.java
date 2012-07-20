package khan.videos.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khan.videos.DAO;
import khan.videos.models.AppUser;
import khan.videos.models.Video;
import khan.videos.models.Video.Status;
import khan.videos.models.Vote;
import khan.videos.servlets.login.BaseUserServlet;
import khan.videos.servlets.templates.Templater;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;

@SuppressWarnings("serial")
public class VoteServlet extends BaseUserServlet {

	public static final Integer REQUIRED_VOTES = 4;
	public static final Double PERCENTAGE_ACCEPT = 0.75;
	public static final Double PERCENTAGE_DENY = 0.6;

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

	// TODO: Test
	public static void runTribunal(Video video, List<Vote> votes) {
		if (votes.size() >= REQUIRED_VOTES) {
			DAO dao = new DAO();
			Double voteTotal = new Double(0);
			for (Vote vote : votes) {
				voteTotal += vote.getWeight();
			}
			Double votePercentage = ((voteTotal + votes.size()) / 2) / votes.size();
			if (votePercentage >= VoteServlet.PERCENTAGE_ACCEPT) {
				// Functioning
				video.setStatus(Status.Accepted);
				dao.ofy().put(video);
			} else if (votePercentage <= VoteServlet.PERCENTAGE_DENY) {
				// Error (?)
				video.setStatus(Status.Denied);
				dao.ofy().put(video);
			}
			System.out.println(votePercentage);
		}
	}

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
				Vote vote = dao.ofy().find(new Key<Vote>(Vote.class, Vote.buildId(video, user)));
				if (vote == null) {
					dao.ofy().put(new Vote(user, video, null, new ArrayList<String>(), accepted, comment));
					req.getSession().setAttribute("message", "Je reactie op de video is opgeslagen, bedankt!");
					resp.sendRedirect("/tribunal");
					List<Vote> votes = dao.ofy().query(Vote.class)
							.filter("video", new Key<Video>(Video.class, video.getYoutubeId())).list();
					VoteServlet.runTribunal(video, votes);
				} else {
					req.getSession().setAttribute("message", "Je hebt al een reactie op deze video geplaatst.");
					resp.sendRedirect("/tribunal");
				}
			} else {
				resp.sendRedirect("/vote?id=" + youtubeId);
			}
		} catch (NotFoundException e) {
			resp.sendRedirect("/tribunal");
		}
	}
}
