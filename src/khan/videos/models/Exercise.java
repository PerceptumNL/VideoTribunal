package khan.videos.models;

import javax.persistence.Id;

public class Exercise {

	@Id
	private String name;
	private String topic;

	public String getTopic() {
		return this.topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getName() {
		return this.name;
	}

	public Exercise(String name, String topic) {
		this.topic = topic;
		this.name = name;
	}

	public Exercise() {
	}

}
