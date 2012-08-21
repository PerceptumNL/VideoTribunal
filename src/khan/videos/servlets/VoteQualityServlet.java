package khan.videos.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khan.videos.DAO;
import khan.videos.models.AppUser;
import khan.videos.models.Video;
import khan.videos.models.Video.Status;
import khan.videos.models.VoteQuality;
import khan.videos.servlets.login.BaseUserServlet;
import khan.videos.servlets.templates.Templater;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;

@SuppressWarnings("serial")
public class VoteQualityServlet extends BaseUserServlet {

	public static final Integer REQUIRED_VOTES = 3;
	public static final Double PERCENTAGE_ACCEPT = 0.7;
	public static final Double PERCENTAGE_DENY = 0.6;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp, AppUser user) throws IOException {
		// Get valid Id
		String id = req.getParameter("id");
		if (id == null || id.length() != 11) {
			resp.sendRedirect("/");
			return;
		}
		// Load video
		try {
			DAO dao = DAO.get();
			Video video = dao.ofy().get(new Key<Video>(Video.class, id));
			List<VoteQuality> votes = dao.ofy().query(VoteQuality.class)
					.filter("video", new Key<Video>(Video.class, id)).list();
			Templater templater = new Templater();
			templater.put("video", video);
			templater.put("votes", votes);
			templater.render("vote.html", req, resp);
		} catch (NotFoundException e) {
			resp.sendRedirect("/");
		}
	}

	// TODO: Test
	public static void runTribunal(Video video) {
		DAO dao = DAO.get();
		List<VoteQuality> votes = dao.ofy().query(VoteQuality.class)
				.filter("video", new Key<Video>(Video.class, video.getYoutubeId())).list();
		if (votes.size() >= REQUIRED_VOTES) {
			Double votesAccept = new Double(0);
			Double votesDenied = new Double(0);
			for (VoteQuality vote : votes) {
				if (vote.getAccepted()) {
					votesAccept++;
				} else {
					votesDenied++;
				}
			}
			Double votePercentage = votesAccept / (votesAccept + votesDenied);
			Status newStatus = Status.Voting;
			if (votePercentage > VoteQualityServlet.PERCENTAGE_ACCEPT) {
				video.setStatus(Status.Accepted);
			} else if (votePercentage < VoteQualityServlet.PERCENTAGE_DENY) {
				newStatus = Status.Denied;
			}
			if (newStatus != video.getStatus()) {
				video.setStatus(newStatus);
				dao.ofy().put(video);
				// TODO: Update video in cache
			}
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp, AppUser user) throws IOException {
		String youtubeId = req.getParameter("youtubeId");
		String status = req.getParameter("status");
		String comment = req.getParameter("comment");
		// Check for illegal status vote
		if ("accepted".equals(status) == false && "denied".equals(status) == false) {
			resp.sendRedirect("/vote?id=" + youtubeId);
			return;
		}
		// Check for non existing video
		DAO dao = DAO.get();
		Video video = dao.ofy().find(Video.class, youtubeId);
		if (video == null) {
			req.getSession().setAttribute("message", "Je probeerde te reageren op een video die er niet meer is.");
			resp.sendRedirect("/");
			return;
		}
		// Check for double vote attempt
		VoteQuality vote = dao.ofy().find(new Key<VoteQuality>(VoteQuality.class, VoteQuality.buildId(video, user)));
		if (vote != null) {
			req.getSession().setAttribute("message", "Je hebt al een reactie op deze video geplaatst.");
			resp.sendRedirect("/");
			return;
		}
		// Place vote
		Boolean accepted = "accepted".equals(status);
		dao.ofy().put(new VoteQuality(user, video, accepted, comment));
		req.getSession().setAttribute("message", "Je reactie op de video is opgeslagen, bedankt!");
		resp.sendRedirect("/");
		VoteQualityServlet.runTribunal(video);
	}

}
