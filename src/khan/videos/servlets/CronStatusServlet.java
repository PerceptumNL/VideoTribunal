package khan.videos.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khan.videos.DAO;
import khan.videos.models.Video;
import khan.videos.models.Video.Status;
import khan.videos.models.Vote;

import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class CronStatusServlet extends HttpServlet {

	public static final Double THRESHOLD = 0.75;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		DAO dao = new DAO();
		List<Video> videos = dao.ofy().query(Video.class).filter("status", Status.Voting).list();
		List<Video> judging = new ArrayList<Video>();
		// Load all videos with 4 or more votes.
		for (Video video : videos) {
			List<Key<Vote>> votekeys = dao.ofy().query(Vote.class)
					.filter("video", new Key<Video>(Video.class, video.getYoutubeId())).listKeys();
			if (votekeys.size() >= 4) {
				judging.add(video);
			}
		}
		// Judge each video depending on votes
		for (Video video : judging) {
			List<Vote> votes = dao.ofy().query(Vote.class)
					.filter("video", new Key<Video>(Video.class, video.getYoutubeId())).list();
			Double total = new Double(0);
			Double thresholdAccept = votes.size() * THRESHOLD;
			for (Vote vote : votes) {
				total += vote.getWeight();
			}
			if (total > thresholdAccept) {
				video.setStatus(Status.Accepted);
			} else {
				video.setStatus(Status.Denied);
			}
			dao.ofy().put(video);
		}
	}
}
