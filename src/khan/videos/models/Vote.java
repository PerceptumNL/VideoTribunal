package khan.videos.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

public class Vote {

	@Id
	private String id;
	private Key<AppUser> user;
	private Key<Video> video;
	private String topic;
	private List<String> exercises;
	private Double weight;
	private String comment;

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Key<AppUser> getUser() {
		return this.user;
	}

	public Key<Video> getVideo() {
		return this.video;
	}

	public String getTopic() {
		return this.topic;
	}

	public List<String> getExercises() {
		return this.exercises;
	}

	public Double getWeight() {
		return this.weight;
	}

	public String getId() {
		return this.id;
	}

	boolean getPositive() {
		return this.weight > 0;
	}

	public Vote() {
	}

	public static String buildId(Video video, AppUser user) {
		return String.format("{%s}{%s}", user.getOpenId(), video.getYoutubeId());
	}

	public Vote(AppUser user, Video video, String topic, List<String> exercises, Boolean accepted, String comment) {
		this.id = Vote.buildId(video, user);
		this.user = new Key<AppUser>(AppUser.class, user.getOpenId());
		this.video = new Key<Video>(Video.class, video.getYoutubeId());
		this.topic = topic;
		this.exercises = new ArrayList<String>(exercises);
		this.weight = (double) Math.min((accepted ? 1 : -1) * (1 + (user.getWeightModifier() / 5)), 1D);
		this.comment = comment;
	}

}
