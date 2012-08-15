package khan.videos.models;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

/**
 * Vote on quality of the video
 */
public class VoteQuality implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private Key<AppUser> user;
	private Key<Video> video;
	private String comment;
	private Boolean accepted;

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

	public String getId() {
		return this.id;
	}

	public Boolean getAccepted() {
		return this.accepted;
	}

	public static String buildId(Video video, AppUser user) {
		return String.format("%s:%s", video.getYoutubeId(), user.getId());
	}

	public VoteQuality(AppUser user, Video video, Boolean accepted, String comment) {
		this.id = VoteQuality.buildId(video, user);
		this.user = new Key<AppUser>(AppUser.class, user.getId());
		this.video = new Key<Video>(Video.class, video.getYoutubeId());
		this.comment = comment;
		this.accepted = accepted;
	}

	public VoteQuality() {
	}

}
