package khan.videos;

import khan.videos.models.AppUser;
import khan.videos.models.Exercise;
import khan.videos.models.Topic;
import khan.videos.models.Video;
import khan.videos.models.Vote;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

public class DAO extends DAOBase {

	static {
		ObjectifyService.register(AppUser.class);
		ObjectifyService.register(Exercise.class);
		ObjectifyService.register(Topic.class);
		ObjectifyService.register(Video.class);
		ObjectifyService.register(Vote.class);
	}

	public AppUser loginAppUser(User gaeuser) {
		AppUser user = ofy().find(AppUser.class, gaeuser.getUserId());
		if (user == null) {
			user = new AppUser(gaeuser.getUserId(), 0);
			ofy().put(user);
		}
		return user;
	}

}
