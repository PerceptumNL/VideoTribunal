package khan.videos.servlets.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khan.videos.DAO;
import khan.videos.models.Video;
import khan.videos.servlets.login.BaseUserServlet;

import com.google.gson.Gson;

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

}