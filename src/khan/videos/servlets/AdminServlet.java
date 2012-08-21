package khan.videos.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khan.videos.DAO;
import khan.videos.models.AppUser;
import khan.videos.models.AppUser.Rank;
import khan.videos.models.Topic;
import khan.videos.models.Video;
import khan.videos.servlets.login.BaseUserServlet;
import khan.videos.servlets.templates.Templater;

import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class AdminServlet extends BaseUserServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp, AppUser user) throws IOException {
		// Check for admin
		if (user.getRank() != Rank.Administrator) {
			req.getSession().setAttribute("message", "Je bent niet ingelogd als administrator.");
			resp.sendRedirect("/");
			return;
		}
		// Display template
		Templater templater = new Templater();
		templater.render("adminpanel.html", req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp, AppUser user) throws IOException {
		// Check for admin
		if (user.getRank() != Rank.Administrator) {
			req.getSession().setAttribute("message", "Je bent niet ingelogd als administrator.");
			resp.sendRedirect("/");
			return;
		}
		// Check if sure
		String action = req.getParameter("action");
		Boolean sure = "on".equals(req.getParameter("sure"));
		if (!sure) {
			req.getSession().setAttribute("message", "Not sure?");
			resp.sendRedirect("/admin");
			return;
		}
		// Run actions
		DAO dao = DAO.get();
		if ("user-remove-videos".equals(action)) {
			List<Key<Video>> videoList = dao.ofy().query(Video.class).filter("user", req.getParameter("userid"))
					.listKeys();
			dao.ofy().delete(videoList);
		} else if ("user-remove-topics".equals(action)) {
			// TODO: Find all videos of topic, and place under root topic's sub
			// Uncategorized
			List<Key<Topic>> topicList = dao.ofy().query(Topic.class).filter("user", req.getParameter("userid"))
					.listKeys();
			dao.ofy().delete(topicList);
		} else if ("user-remove-all".equals(action)) {
			// TODO: Remove all user related data & blacklist user
			// TODO: Remove votes & recalculate tribunal per voted video
		} else if ("video-recategorize-all".equals(action)) {
			String topic = req.getParameter("topic");
			List<Video> videoList = dao.ofy().query(Video.class).list();
			for (Video video : videoList) {
				video.setTopic(new Key<Topic>(Topic.class, topic));
			}
			dao.ofy().put(videoList);
		} else if ("video-recategorize".equals(action)) {
			String topic = req.getParameter("topic");
			String ytid = req.getParameter("ytid");
			Video video = dao.ofy().find(new Key<Video>(Video.class, ytid));
			video.setTopic(new Key<Topic>(Topic.class, topic));
			dao.ofy().put(video);
		} else if ("video-remove".equals(action)) {
			String ytid = req.getParameter("ytid");
			Video video = dao.ofy().find(new Key<Video>(Video.class, ytid));
			dao.ofy().delete(video);
		}
		MemcacheServiceFactory.getMemcacheService().clearAll();
		req.getSession().setAttribute("message", "Done.");
		resp.sendRedirect("/admin");
	}

}