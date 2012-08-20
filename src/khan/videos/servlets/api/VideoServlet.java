package khan.videos.servlets.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khan.videos.DAO;
import khan.videos.YoutubeHelper;
import khan.videos.models.AppUser;
import khan.videos.models.Topic;
import khan.videos.models.Video;
import khan.videos.servlets.login.BaseUserServlet;

import com.google.gson.Gson;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class VideoServlet extends BaseUserServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// Load Videos
		String topicName = req.getParameter("topic");
		List<Video> videos = DAO.get().getTopicVideos(topicName);
		// Send as JSON
		Gson gson = new Gson();
		resp.setContentType("application/json");
		resp.getOutputStream().print(gson.toJson(videos));
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp, AppUser user) throws IOException {
		// Check for youtube url
		String url = req.getParameter("youtubeId");
		if (url == null || url.startsWith("http://youtu.be/") == false) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Vul AUB een youtu.be share URL in.");
			return;
		}
		String youtubeId = url.substring("http://youtu.be/".length());
		if (youtubeId.matches("[a-zA-z0-9-]{11}") == false) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Vul AUB een youtu.be share URL in.");
			return;
		}
		// Check if video exists & title can be extracted
		String title = YoutubeHelper.getTitle(youtubeId);
		if (title == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Het is niet gelukt om gegevens van youtube op te halen over deze video.");
			return;
		}
		if (YoutubeHelper.isEmbeddable(youtubeId) == false) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Deze video is niet embeddable; hierdoor kan de video niet geplaatst worden.");
			return;
		}
		// Check for double entry
		DAO dao = DAO.get();
		Video existing = dao.ofy().find(Video.class, youtubeId);
		if (existing != null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Deze video is al ingezonden.");
			return;
		}
		// Check for root entry
		String topicname = req.getParameter("topic");
		if (topicname == null || topicname.length() == 0) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Je kunt videos alleen onder een vak of onderdeel toevoegen.");
			return;
		}
		// Check if topic exists
		Topic topic = dao.ofy().find(Topic.class, topicname);
		if (topic == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dit onderdeel bestaat niet (meer).");
			return;
		}
		// Success
		Video video = new Video(youtubeId, req.getRemoteAddr(), new Key<AppUser>(AppUser.class, user.getId()),
				new Key<Topic>(Topic.class, topicname), title);
		dao.addVideo(video);
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.getOutputStream().print("/vote?id=" + youtubeId);
	}

}