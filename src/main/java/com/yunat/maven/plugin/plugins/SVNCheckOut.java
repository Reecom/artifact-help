package com.yunat.maven.plugin.plugins;

import java.io.File;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNCheckOut {
	private static SVNClientManager svnClientManager;
	private String username;
	private String password;
	private String svnUrl;
	private String downloadDir;

	public SVNCheckOut(String username, String password, String svnUrl,
			String downloadDir) {
		super();
		this.username = username;
		this.password = password;
		this.svnUrl = svnUrl;
		this.downloadDir = downloadDir;
	}

	public void co() throws Exception {
		SVNRepositoryFactoryImpl.setup();
		
		SVNURL repositoryURL = null;
		try {
			repositoryURL = SVNURL.parseURIEncoded(svnUrl);
		} catch (SVNException e) {
			e.printStackTrace();
		}
		
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		
		svnClientManager = SVNClientManager.newInstance((DefaultSVNOptions)options, username, password);
		
		File dlDor = new File(downloadDir);
		
		SVNUpdateClient updateClient = svnClientManager.getUpdateClient();
		
		updateClient.setIgnoreExternals(false);
		
		long workingVersion = updateClient.doCheckout(repositoryURL, dlDor, 
				SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, false);
		System.out.println("把版本: " + workingVersion + " check out 到目录: " + dlDor + "中");
	}
	
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

	public String getSvnUrl() {
		return svnUrl;
	}

	public void setSvnUrl(String svnUrl) {
		this.svnUrl = svnUrl;
	}

	public String getDownloadDir() {
		return downloadDir;
	}

	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}

	
}
