package khan.videos.models;

import java.io.Serializable;

import javax.persistence.Id;

public class AppUser implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Rank {
		User, Administrator
	};

	private Rank rank;

	@Id
	private String loginProviderId;
	private Provider loginProvider;

	public enum Provider {
		Facebook, Linkedin, Google, Twitter
	};

	private String linkFacebook;
	private String linkGoogle;
	private String linkTwitter;
	private String linkLinkedin;

	public String getLinkFacebook() {
		return this.linkFacebook;
	}

	public void setLinkFacebook(String linkFacebook) {
		this.linkFacebook = linkFacebook;
	}

	public String getLinkGoogle() {
		return this.linkGoogle;
	}

	public void setLinkGoogle(String linkGoogle) {
		this.linkGoogle = linkGoogle;
	}

	public String getLinkTwitter() {
		return this.linkTwitter;
	}

	public void setLinkTwitter(String linkTwitter) {
		this.linkTwitter = linkTwitter;
	}

	public String getLinkLinkedin() {
		return this.linkLinkedin;
	}

	public void setLinkLinkedin(String linkLinkedin) {
		this.linkLinkedin = linkLinkedin;
	}

	public Provider getLoginProvider() {
		return this.loginProvider;
	}

	public String getId() {
		return this.loginProviderId;
	}

	public Rank getRank() {
		return this.rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public AppUser(Provider loginProvider, String loginProviderId) {
		this.rank = Rank.User;
		this.loginProvider = loginProvider;
		this.loginProviderId = loginProviderId;
		this.linkFacebook = null;
		this.linkGoogle = null;
		this.linkTwitter = null;
		this.linkLinkedin = null;
	}

	public AppUser() {
	}

}
