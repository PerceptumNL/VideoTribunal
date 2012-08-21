package khan.videos.servlets.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khan.videos.DAO;
import khan.videos.models.AppUser;
import khan.videos.models.Topic;
import khan.videos.servlets.login.BaseUserServlet;

import com.google.gson.Gson;

@SuppressWarnings("serial")
public class TopicServlet extends BaseUserServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// Load topics
		String topicName = req.getParameter("topic");
		List<Topic> topics = DAO.get().getTopicChildren(topicName);
		// Send as JSON
		Gson gson = new Gson();
		resp.setContentType("application/json");
		resp.getOutputStream().print(gson.toJson(topics));
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp, AppUser user) throws IOException {
		// Check for admin
		if (user.getRank() != AppUser.Rank.Administrator) {
			resp.setHeader("message", "Topics toevoegen is momenteel alleen voor admins.");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		// Check topic name
		String topicName = req.getParameter("topicName");
		String topicNameParent = req.getParameter("topic");
		if (topicName == null || topicName.length() == 0) {
			resp.setHeader("message", "Voer een naam in voor het nieuwe onderdeel.");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		// Check < and > characters
		if (topicName.contains("<") || topicName.contains(">")) {
			resp.setHeader("message", "\"<\" en \">\" tekens zijn niet toegestaan in onderdeel namen.");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		// Check for max length
		if (topicName.length() > 60) {
			resp.setHeader("message", "De maximale lengte voor namen van onderdelen is 60 karakters.");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		// Check for double entry
		DAO dao = DAO.get();
		Topic existing = dao.ofy().find(Topic.class, topicName);
		if (existing != null) {
			resp.setHeader("message", "Er bestaat al een onderdeel met deze naam.");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		// Check if parent topic; if parent topic exists
		Topic parent = null;
		if (topicNameParent != null && topicNameParent.length() > 0) {
			parent = dao.ofy().find(Topic.class, topicNameParent);
			if (parent == null) {
				resp.setHeader("message",
						"Het onderdeel waar je je nieuwe onderdeel aan wilt toevoegen bestaat niet (meer).");
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		}
		// Success
		Topic topic = new Topic(topicName, user, parent);
		dao.addTopic(topic);
		resp.setStatus(HttpServletResponse.SC_OK);
	}

}