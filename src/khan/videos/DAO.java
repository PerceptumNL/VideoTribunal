package khan.videos;

import java.util.ArrayList;
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
	public AppUser loginAppUser(User gaeuser, boolean isAdmin) {
		AppUser user = ofy().find(AppUser.class, gaeuser.getUserId());
		if (user == null) {
			user = new AppUser(AppUser.Provider.Google, gaeuser.getUserId(), gaeuser.getEmail());
			ofy().put(user);
		}
		user.setRank(isAdmin ? AppUser.Rank.Administrator : AppUser.Rank.User);
		return user;
	}

	// -- Topic -------------------------------------------

	private static String makeTopicChildrenKey(String topic) {
		return String.format("TopicChildren/%s", topic == null ? "" : topic);
	}

	public void addTopic(Topic topic) {
		this.ofy().put(topic);
		// Edit children list of topic's parent
		String parentName = topic.getParent() == null ? null : topic.getParent().getName();
		List<Topic> topics = this.getTopicChildren(parentName);
		for (Topic child : topics) {
			if (child.getName().equals(topic.getName())) {
				return;
			}
		}
		topics.add(topic);
		memcache.put(DAO.makeTopicChildrenKey(parentName), topics);
	}

	/**
	 * @param parent
	 *            if null: returns topics without parent ( "root topics" )
	 */
	@SuppressWarnings("unchecked")
	public List<Topic> getTopicChildren(String parent) {
		List<Topic> children = (List<Topic>) memcache.get(DAO.makeTopicChildrenKey(parent));
		if (children == null) {
			Key<Topic> parentKey = parent == null ? null : new Key<Topic>(Topic.class, parent);
			children = this.ofy().query(Topic.class).filter("parent", parentKey).list();
			if (children == null) {
				children = new ArrayList<Topic>();
			}
			memcache.put(DAO.makeTopicChildrenKey(parent), children);
		}
		return children;
	}

	// -- Videos ------------------------------------------

	private static String makeTopicVideosKey(String topic) {
		return String.format("TopicVideos/%s", topic == null ? "" : topic);
	}

	public void addVideo(Video video) {
		this.ofy().put(video);
		// Edit children list of video's parent
		String parentName = video.getTopic() == null ? null : video.getTopic().getName();
		List<Video> videos = this.getTopicVideos(parentName);
		// Check if already in list
		for (Video child : videos) {
			if (child.getYoutubeId().equals(video.getYoutubeId())) {
				return;
			}
		}
		videos.add(video);
		memcache.put(DAO.makeTopicVideosKey(parentName), videos);
	}

	/**
	 * @param parent
	 *            if null: returns videos without parent ( "root videos" )
	 */
	@SuppressWarnings("unchecked")
	public List<Video> getTopicVideos(String parent) {
		List<Video> children = (List<Video>) memcache.get(DAO.makeTopicVideosKey(parent));
		if (children == null) {
			Key<Topic> parentKey = parent == null ? null : new Key<Topic>(Topic.class, parent);
			children = this.ofy().query(Video.class).filter("topic", parentKey).filter("status", "voting").list();
			if (children == null) {
				children = new ArrayList<Video>();
			}
			memcache.put(DAO.makeTopicVideosKey(parent), children);
		}
		return children == null ? new ArrayList<Video>() : children;
	}

	// -- Topic Tree --------------------------------------

	// TODO: Topic Tree

}
