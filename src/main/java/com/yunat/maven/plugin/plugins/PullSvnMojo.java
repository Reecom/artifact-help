package com.yunat.maven.plugin.plugins;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * @author yangtao
 * @goal compile
 * @phase package
 * @requiresProject false
 */
public class PullSvnMojo extends AbstractMojo {
	/**
	 * @parameter
     * @required
	 */
	private String path;
	
	/**
	 * @parameter
	 */
	private SVNConfig svnconfig;
	
	private String[] pullDirs = new String[]{PullDir.trunk.toString(), 
			PullDir.branches.toString(), PullDir.tags.toString()};
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("path: " + path);
		if (null != svnconfig) {
			getLog().info("SVN username: " + svnconfig.getUsername());
			getLog().info("SVN password: " + svnconfig.getPassword());
			getLog().info("SVN dir: " + svnconfig.getPulldir());
			getLog().info("SVN version: " + svnconfig.getVersion());
			
			if (null != svnconfig.getUrls()) {
				List<String> urls = svnconfig.getUrls();
				for (String url : urls) {
					getLog().info("SVN url: " + url);
				}
			}
		}
		
		String realPath = path.substring(0, path.lastIndexOf(File.separator));
		String urlSuffix = null;
		if (ArrayUtils.contains(pullDirs, svnconfig.getPulldir())) {
			if (PullDir.trunk.toString().equals(svnconfig.getPulldir())) {
				urlSuffix = "/" + PullDir.trunk.toString();
			} else {
				if (StringUtils.isNotEmpty(svnconfig.getVersion())) {
					urlSuffix = "/" + svnconfig.getPulldir() + "/" + svnconfig.getVersion();
				}
			}
		}
		
		if (null == urlSuffix) {
			getLog().error("svnconfig is error");
		}
		
		if (null != svnconfig.getUrls()) {
			List<String> urls = svnconfig.getUrls();
			// svn checkout
			for (String url : urls) {
				String downloadDir = realPath + File.separator + url.substring(url.lastIndexOf("/"));
				if (StringUtils.isNotEmpty(svnconfig.getVersion())) {
					downloadDir += "-" + svnconfig.getVersion();
				}
				
				SVNCheckOut svnco = new SVNCheckOut(svnconfig.getUsername(), svnconfig.getPassword(), 
						url + urlSuffix, downloadDir);
				try {
					svnco.co();
					//branches need modify versions
					if (PullDir.branches.toString().equals(svnconfig.getPulldir())) {
						// read pom.xml 
						boolean isWrite = BizHelper.modifyPomVersion(downloadDir, svnconfig.getVersion() + "-SNAPSHOT");
						if (isWrite) {
							getLog().info("@-- success modify pom.xml ---");
							//commit svn
							SVNCommit svncommit = new SVNCommit(svnconfig.getUsername(), svnconfig.getPassword(), 
									url + urlSuffix, downloadDir);
							try {
								svncommit.commit();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
				
			// maven eclipse plugin execute
			for (String url : urls) {
				String downloadDir = realPath + File.separator + url.substring(url.lastIndexOf("/"));
				if (StringUtils.isNotEmpty(svnconfig.getVersion())) {
					downloadDir += "-" + svnconfig.getVersion();
				}
				
				// eclipse 设置 执行mvn eclipse:eclipse
				MavenExecEclipse mee = new MavenExecEclipse();
				try {
					mee.execute("mvn eclipse:eclipse", downloadDir);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
