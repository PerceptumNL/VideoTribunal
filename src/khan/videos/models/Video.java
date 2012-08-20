package khan.videos.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.googlecode.objectify.Key;

public class Video implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Status {
		Voting, Accepted, Denied
	}

	@Id
	private String youtubeId;
	private Date submitted;
	@Transient
	private String submitIPAddress;
	@Transient
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

	public String getSubmitIPAddress() {
		return this.submitIPAddress;
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

	public Video(String youtubeId, String submitIPAddress, Key<AppUser> user, Key<Topic> topic, String title) {
		this.topic = topic;
		this.youtubeId = youtubeId;
		this.submitted = Calendar.getInstance().getTime();
		this.submitIPAddress = submitIPAddress;
		this.user = user;
		this.title = title;
		this.status = Status.Voting;
	}

	public Video() {
	}

}
