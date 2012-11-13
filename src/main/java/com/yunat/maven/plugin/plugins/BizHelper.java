package com.yunat.maven.plugin.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class BizHelper {
	
	@SuppressWarnings("rawtypes")
	public static boolean modifyPomVersion(String dirPath, String versionNo) {
		if (dirPath.indexOf(versionNo) > -1) {
			return false;
		}
		String pomXML = dirPath + File.separator + "pom.xml";
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(pomXML);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		if (null != document) {
			boolean isWrite = false;
			List list = document.selectNodes("//project");
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Element element = (Element)iter.next();
				Iterator iterator = element.elementIterator("version");
				while (iterator.hasNext()) {
					Element versionElement = (Element)iterator.next();
					if (!versionElement.getText().equals(versionNo)) {
						versionElement.setText(versionNo);
						isWrite = true;
					}
				}
			}
			
			if (isWrite) {
				XMLWriter output;
				try {
					output = new XMLWriter(new FileWriter(new File(pomXML)));
					output.write(document);
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 执行mvn eclipse:eclipse
	 * @return
	 */
	public static boolean execMvnEclipse(String dirPath, String commandLine)  {
		CommandLine cmdLine = CommandLine.parse(commandLine);
		Executor executor = new DefaultExecutor();
		executor.setWorkingDirectory(new File(dirPath));
		executor.setExitValue(1);
		ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
		//DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		executor.setWatchdog(watchdog);
		try {
			//executor.execute(cmdLine, resultHandler);
			//resultHandler.waitFor(60000);
			//int exitValue = resultHandler.getExitValue();
			int exitValue = executor.execute(cmdLine);
			if (exitValue == 1) {
				return true;
			}
		} catch (ExecuteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}