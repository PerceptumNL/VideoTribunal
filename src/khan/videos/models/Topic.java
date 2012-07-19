package khan.videos.models;

import javax.persistence.Id;

public class Topic {

	@Id
	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Topic(String name) {
		this.name = name;
	}

	public Topic() {
	}

}
