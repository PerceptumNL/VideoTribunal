package khan.videos.models;

import java.io.Serializable;

import javax.persistence.Id;

public class AppUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String openId;
	private Integer weightModifier;

	public String getOpenId() {
		return this.openId;
	}

	public Integer getWeightModifier() {
		return this.weightModifier;
	}

	public void setWeightModifier(Integer weightModifier) {
		this.weightModifier = weightModifier;
	}

	public AppUser(String openId, Integer weightModifier) {
		this.openId = openId;
		this.weightModifier = weightModifier;
	}

	public AppUser() {
	}

}