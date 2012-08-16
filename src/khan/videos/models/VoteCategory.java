package khan.videos.models;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

/**
 * Vote / Note on category error of video
 */
public class VoteCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private Key<AppUser> user;
	private Key<Video> video;
	private Key<Topic> topic;

	public Key<AppUser> getUser() {
		return this.user;
	}

	public Key<Video> getVideo() {
		return this.video;
	}

	public Key<Topic> getTopic() {
		return this.topic;
	}

	public String getId() {
		return this.id;
	}

	private static String buildId(Topic topic, Video video, AppUser user) {
		return String.format("%s:%s:%s", video.getYoutubeId(), user.getId(), topic.getName());
	}

	private static String buildId(String topic, String video, String user) {
		return String.format("%s:%s:%s", video, user, topic);
	}

	public VoteCategory(String userId, String videoId, String topicId) {
		this.id = VoteCategory.buildId(topicId, videoId, userId);
		this.user = new Key<AppUser>(AppUser.class, userId);
		this.video = new Key<Video>(Video.class, videoId);
		this.topic = new Key<Topic>(Topic.class, topicId);
	}

	public VoteCategory(AppUser user, Video video, Topic topic) {
		this.id = VoteCategory.buildId(topic, video, user);
		this.user = new Key<AppUser>(AppUser.class, user.getId());
		this.video = new Key<Video>(Video.class, video.getYoutubeId());
		this.topic = new Key<Topic>(Topic.class, topic.getName());
	}

	public VoteCategory() {
	}

}
