package com.yunat.maven.plugin.plugins;

import java.util.List;


public class SVNConfig {
	private String username;
	private String password;
	private String pulldir;
	private String version;
	private List<String> urls;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPulldir() {
		return pulldir;
	}

	public void setPulldir(String pulldir) {
		this.pulldir = pulldir;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

}
