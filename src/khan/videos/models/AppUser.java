package khan.videos.models;

import java.io.Serializable;

import javax.persistence.Id;

public class AppUser implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Provider {
		Facebook, Linkedin, Google, Twitter
	};

	@Id
	private String loginProviderId;
	private Provider loginProvider;
	// Links to profile
	private String linkFacebook = null;
	private String linkGoogle = null;
	private String linkTwitter = null;
	private String linkLinkedin = null;

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

	public AppUser(Provider loginProvider, String loginProviderId) {
		this.loginProvider = loginProvider;
		this.loginProviderId = loginProviderId;
	}

	public AppUser() {
	}

}
