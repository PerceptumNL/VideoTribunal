package khan.videos.servlets;

import java.io.IOException;
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

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		DAO dao = new DAO();
		List<Video> videos = dao.ofy().query(Video.class).filter("status", Status.Voting).list();
		for (Video video : videos) {
			List<Vote> votes = dao.ofy().query(Vote.class)
					.filter("video", new Key<Video>(Video.class, video.getYoutubeId())).list();
			if (votes.size() > 4) {
				// ...
			}
		}
	}
}
