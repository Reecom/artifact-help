package com.yunat.maven.plugin.plugins;import static org.junit.Assert.*;import org.junit.Test;public class SVNCommitTest {	@Test	public void test() {		fail("Not yet implemented");	}	@Test	public void testCommit() {		SVNCommit svncommit = new SVNCommit("tao.yang", "qazwsxedc", 				"http://svn.yunat.com/ccms/web/ccms_common/branches",				"/Users/yangtao/maven-plugin/ccms_common-3.0.6.5");		try {			svncommit.commit();		} catch (Exception e) {			e.printStackTrace();		}	}}