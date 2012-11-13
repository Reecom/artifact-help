package com.yunat.maven.plugin.plugins;

import java.io.File;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNCommit {
	private static SVNClientManager svnClientManager;
	private String username;
	private String password;
	private String downloadDir;
	private String svnUrl;

	public SVNCommit(String username, String password, String svnUrl, 
			String downloadDir) {
		super();
		this.username = username;
		this.password = password;
		this.svnUrl = svnUrl;
		this.downloadDir = downloadDir;
	}

	@SuppressWarnings("unused")
	public void commit() throws Exception {
		SVNRepositoryFactoryImpl.setup();
		
		SVNURL repositoryURL = null;
		try {
			repositoryURL = SVNURL.parseURIEncoded(svnUrl);
		} catch (SVNException e) {
			e.printStackTrace();
		}
		
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		
		svnClientManager = SVNClientManager.newInstance((DefaultSVNOptions)options, username, password);
		
		File commitFile = new File(downloadDir + File.separator + "pom.xml");
		SVNStatus status = svnClientManager.getStatusClient().doStatus(commitFile, true);
		
		if (null != status) {
			if (status.getContentsStatus().equals(SVNStatusType.STATUS_UNVERSIONED)) {
				svnClientManager.getWCClient().doAdd(commitFile, false, false, 
						false, SVNDepth.INFINITY, false, false);
				
				svnClientManager.getCommitClient().doCommit(new File[]{commitFile}, 
						true, "issue:none\r\nmsg:none", null, null, true, false, SVNDepth.INFINITY);
				System.out.println("svn add");
			} else {
				svnClientManager.getCommitClient().doCommit(new File[]{commitFile}, 
						true, "issue:none\r\nmsg:none", null, null, true, false, SVNDepth.INFINITY);
				System.out.println("svn commit");
			}
			
			System.out.println(status.getContentsStatus().toString());
		}
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

	public String getDownloadDir() {
		return downloadDir;
	}

	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}

	public String getSvnUrl() {
		return svnUrl;
	}

	public void setSvnUrl(String svnUrl) {
		this.svnUrl = svnUrl;
	}
	
}
