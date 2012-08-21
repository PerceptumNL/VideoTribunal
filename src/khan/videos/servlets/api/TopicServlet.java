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
		// Check topic name
		String topicName = req.getParameter("topicName");
		String topicNameParent = req.getParameter("topic");
		if (topicName == null || topicName.length() == 0) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Voer een naam in voor het nieuwe onderdeel.");
			return;
		}
		// Check < and > characters
		if (topicName.contains("<") || topicName.contains(">")) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"\"<\" en \">\" tekens zijn niet toegestaan in onderdeel namen.");
			return;
		}
		// Check for max length
		if (topicName.length() > 60) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"De maximale lengte voor namen van onderdelen is 60 karakters.");
			return;
		}
		// Check for double entry
		DAO dao = DAO.get();
		Topic existing = dao.ofy().find(Topic.class, topicName);
		if (existing != null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Er bestaat al een onderdeel met deze naam.");
			return;
		}
		// Check if parent topic; if parent topic exists
		Topic parent = null;
		if (topicNameParent != null && topicNameParent.length() > 0) {
			parent = dao.ofy().find(Topic.class, topicNameParent);
			if (parent == null) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Het onderdeel waar je je nieuwe onderdeel aan wilt toevoegen bestaat niet (meer).");
				return;
			}
		}
		// Success
		Topic topic = new Topic(topicName, user, parent);
		dao.addTopic(topic);
		resp.setStatus(HttpServletResponse.SC_OK);
	}

}