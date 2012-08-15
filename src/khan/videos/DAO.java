package khan.videos;

import java.util.List;

import khan.videos.models.AppUser;
import khan.videos.models.Topic;
import khan.videos.models.Video;
import khan.videos.models.VoteCategory;
import khan.videos.models.VoteQuality;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

public class DAO extends DAOBase {

	// -- Singleton Stuff ---------------------------------

	private final MemcacheService memcache;

	private static DAO instance;

	public static DAO get() {
		if (instance == null) {
			instance = new DAO();
		}
		return instance;
	}

	private DAO() {
		memcache = MemcacheServiceFactory.getMemcacheService();
		ObjectifyService.register(AppUser.class);
		ObjectifyService.register(Video.class);
		ObjectifyService.register(Topic.class);
		ObjectifyService.register(VoteQuality.class);
		ObjectifyService.register(VoteCategory.class);
	}

	// -- AppUser -----------------------------------------

	// TODO: New user creation via providers
	public AppUser loginAppUser(User gaeuser) {
		AppUser user = ofy().find(AppUser.class, gaeuser.getUserId());
		if (user == null) {
			user = new AppUser(AppUser.Provider.Google, gaeuser.getUserId());
			ofy().put(user);
		}
		return user;
	}

	// -- Topic -------------------------------------------

	private static String makeTopicChildrenKey(Topic topic) {
		return String.format("TopicChildren/%s", topic.getName() == null ? "" : topic.getName());
	}

	// TODO: Test
	public void addTopic(Topic topic) {
		this.ofy().put(topic);
		// Edit children list of topic's parent
		Topic parent = topic.getParent();
		List<Topic> topics = this.getTopicChildren(parent);
		topics.add(topic);
		memcache.put(DAO.makeTopicChildrenKey(parent), topics);
	}

	/**
	 * @param parent
	 *            if null: returns topics without parent ( "root topics" )
	 */
	// TODO: Test with and without parent
	@SuppressWarnings("unchecked")
	public List<Topic> getTopicChildren(Topic parent) {
		List<Topic> children = (List<Topic>) memcache.get(DAO.makeTopicChildrenKey(parent));
		if (children == null) {
			this.ofy().query(Topic.class).filter("parent", parent);
			memcache.put(DAO.makeTopicChildrenKey(parent), children);
		}
		return children;
	}

	// -- Videos ------------------------------------------

	private static String makeTopicVideosKey(Topic topic) {
		return String.format("TopicVideos/%s", topic.getName() == null ? "" : topic.getName());
	}

	// TODO: Test
	public void addVideo(Video video) {
		this.ofy().put(video);
		// Edit children list of video's parent
		Key<Topic> parentKey = video.getTopic();
		Topic parent = this.ofy().get(parentKey);
		List<Video> videos = this.getTopicVideos(parent);
		videos.add(video);
		memcache.put(DAO.makeTopicVideosKey(parent), videos);
	}

	/**
	 * @param parent
	 *            if null: returns videos without parent ( "root videos" )
	 */
	// TODO: Test with and without parent
	@SuppressWarnings("unchecked")
	public List<Video> getTopicVideos(Topic parent) {
		List<Video> children = (List<Video>) memcache.get(DAO.makeTopicVideosKey(parent));
		if (children == null) {
			this.ofy().query(Video.class).filter("topic", parent);
			memcache.put(DAO.makeTopicVideosKey(parent), children);
		}
		return children;
	}

}
