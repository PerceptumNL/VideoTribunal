package khan.videos.servlets;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khan.videos.DAO;
import khan.videos.YoutubeHelper;
import khan.videos.models.AppUser;
import khan.videos.models.Video;
import khan.videos.servlets.login.BaseUserServlet;
import khan.videos.servlets.templates.Templater;

import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class VideoServlet extends BaseUserServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp, AppUser user) throws IOException {
		Templater templater = new Templater();
		if (req.getParameter("message") != null) {
			templater.put("message", req.getParameter("message"));
		}
		templater.render("submit.html", req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp, AppUser user) throws IOException {
		// Check for youtube url
		String url = req.getParameter("yt-id");
		if (url == null || url.startsWith("http://youtu.be/") == false) {
			resp.sendRedirect("/submit?message=" + URLEncoder.encode("Vul AUB een youtu.be share URL in.", "UTF-8"));
			return;
		}
		String youtubeId = url.substring("http://youtu.be/".length());
		if (youtubeId.matches("[a-zA-z0-9-]{11}") == false) {
			resp.sendRedirect("/submit?message=" + URLEncoder.encode("Vul AUB een youtu.be share URL in.", "UTF-8"));
			return;
		}
		String title = YoutubeHelper.getTitle(youtubeId);
		if (title == null) {
			resp.sendRedirect("/submit?message="
					+ URLEncoder
							.encode("Het is niet gelukt om gegevens van youtube op te halen over deze video - Let er goed op dat de Id code klopt.",
									"UTF-8"));
			return;
		}
		// Check for existing, then create new.
		DAO dao = DAO.get();
		Video existing = dao.ofy().find(Video.class, youtubeId);
		if (existing == null) {
			Video video = new Video(youtubeId, req.getRemoteAddr(), new Key<AppUser>(AppUser.class, user.getId()),
					null, title);
			dao.ofy().put(video);
		} else {
			req.getSession().setAttribute("message", "Deze video is al door iemand anders ingezonden.");
		}
		resp.sendRedirect("vote?id=" + youtubeId);
	}

}
