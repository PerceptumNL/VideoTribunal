package khan.videos.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Id;

public class Topic implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	// Creation data
	private AppUser user;
	private Date created;
	private Topic parent = null;

	public Topic getParent() {
		return this.parent;
	}

	public String getName() {
		return this.name;
	}

	public AppUser getUser() {
		return this.user;
	}

	public Date getCreated() {
		return this.created;
	}

	public Topic() {
	}

	public Topic(String name, AppUser user, Topic parent) {
		this.parent = parent;
		this.name = name;
		this.user = user;
		this.created = Calendar.getInstance().getTime();
	}

}
