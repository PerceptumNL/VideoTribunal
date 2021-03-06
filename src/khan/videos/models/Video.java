package khan.videos.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

public class Video implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Status {
		Voting, Accepted, Denied
	}

	@Id
	private String youtubeId;
	private Date submitted;
	private Key<AppUser> user;
	private Key<Topic> topic;
	private String title;
	private Status status;

	public Key<Topic> getTopic() {
		return this.topic;
	}

	public String getTitle() {
		return this.title;
	}

	public String getYoutubeId() {
		return this.youtubeId;
	}

	public Date getSubmitted() {
		return this.submitted;
	}

	public Key<AppUser> getUser() {
		return this.user;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setTopic(Key<Topic> topic) {
		this.topic = topic;
	}

	public Video(String youtubeId, Key<AppUser> user, Key<Topic> topic, String title) {
		this.topic = topic;
		this.youtubeId = youtubeId;
		this.submitted = Calendar.getInstance().getTime();
		this.user = user;
		this.title = title;
		this.status = Status.Voting;
	}

	public Video() {
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Video) {
			return ((Video) o).getYoutubeId().equals(this.getYoutubeId());
		}
		return false;
	}
}
