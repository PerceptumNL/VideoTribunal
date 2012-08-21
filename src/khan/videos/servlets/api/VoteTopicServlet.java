package khan.videos.servlets.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khan.videos.DAO;
import khan.videos.models.AppUser;
import khan.videos.models.Topic;
import khan.videos.models.Video;
import khan.videos.models.VoteTopic;
import khan.videos.servlets.login.BaseUserServlet;

import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class VoteTopicServlet extends BaseUserServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp, AppUser user) throws IOException {

		String topicName = req.getParameter("topic");
		String youtubeId = req.getParameter("youtubeId");

		DAO dao = DAO.get();

		// Check topic
		if (topicName == null) {
			resp.setHeader("message", "Kies eerst een onderdeel");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		// Check topic datastore
		Topic topic = dao.ofy().find(new Key<Topic>(Topic.class, topicName));
		if (topic == null) {
			resp.setHeader("message", "Je hebt een onderdeel gekozen wat niet (meer) bestaat");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		// Check video
		Video video = dao.ofy().find(new Key<Video>(Video.class, youtubeId));
		if (video == null) {
			resp.setHeader("message", "Je hebt een video gekozen die niet (meer) bestaat");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		// Check for double entry
		VoteTopic voteDouble = dao.ofy().find(new Key<VoteTopic>(VoteTopic.class, VoteTopic.buildId(video, user)));
		if (voteDouble != null) {
			resp.setHeader("message", "Je hebt al topic wijziging doorgegeven op deze video.");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		// Success
		VoteTopic vote = new VoteTopic(user, video, topic);
		dao.ofy().put(vote);
		// TODO: Handle topic change by amount of votes ( ? Topic tribunal )
		video.setTopic(new Key<Topic>(Topic.class, topicName));
		dao.addVideo(video);
		resp.setStatus(HttpServletResponse.SC_OK);
	}
}