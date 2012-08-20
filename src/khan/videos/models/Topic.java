package khan.videos.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.googlecode.objectify.Key;

public class Topic implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	// Creation data
	@Transient
	private Key<AppUser> user;
	private Date created;
	@Transient
	private Key<Topic> parent = null;

	public Key<Topic> getParent() {
		return this.parent;
	}

	public String getName() {
		return this.name;
	}

	public Key<AppUser> getUser() {
		return this.user;
	}

	public Date getCreated() {
		return this.created;
	}

	public Topic() {
	}

	public Topic(String name, Key<AppUser> user, Key<Topic> parent) {
		this.parent = parent;
		this.name = name;
		this.user = user;
		this.created = Calendar.getInstance().getTime();
	}

	public Topic(String name, AppUser user, Topic parent) {
		this.parent = parent == null ? null : new Key<Topic>(Topic.class, parent.getName());
		this.name = name;
		this.user = user == null ? null : new Key<AppUser>(AppUser.class, user.getId());
		this.created = Calendar.getInstance().getTime();
	}

}
