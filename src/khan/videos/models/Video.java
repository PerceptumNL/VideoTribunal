package khan.videos.models;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

public class Video {

	public enum Status {
		Voting, Accepted, Denied;
	}

	@Id
	private String youtubeId;
	private Date submitted;
	private String submitIPAddress;
	private Key<AppUser> user;
	private Status status;
	private String title;

	public String getTitle() {
		return this.title;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public Video(String youtubeId, String submitIPAddress, Key<AppUser> user, String title) {
		this.youtubeId = youtubeId;
		this.submitted = Calendar.getInstance().getTime();
		this.submitIPAddress = submitIPAddress;
		this.user = user;
		this.status = Video.Status.Voting;
		this.title = title;
	}

	public Video() {
	}

}
