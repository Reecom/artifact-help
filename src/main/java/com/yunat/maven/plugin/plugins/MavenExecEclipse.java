package com.yunat.maven.plugin.plugins;

import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;

public class MavenExecEclipse {
	private final static long printJobTimeout = 15000;
	private final static boolean printInBackground = true;

	public void execute(String line, String fileDir) throws Exception {
		PrintResultHandler printResult = null;
		try {
			System.out.println("[MavenExecEclipse] Preparing print do ...");
			printResult = print(line, fileDir, printJobTimeout, printInBackground);
			System.out.println("[MavenExecEclipse] Successfully sent the print job ...");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[MavenExecEclipse] Printig of the follwing document failed ... ");
		}

		if (printResult != null) {
			System.out.println("[MavenExecEclipse] execute is exiting but waiting for the print job to finish ...");
			printResult.waitFor();
			System.out.println("[MavenExecEclipse] the print job has finished ...");
		}
	}

	private PrintResultHandler print(String line, String fileDir, long printJobTimeout, boolean printInBackground) throws IOException {
		int exitValue;
		ExecuteWatchdog watchdog = null;
		PrintResultHandler resultHandler;

		CommandLine commandLine = CommandLineUtils.artifact(fileDir, line);
		Executor executor = new DefaultExecutor();
		executor.setWorkingDirectory(new File(fileDir));
		executor.setExitValue(1);

		if (printJobTimeout > 0) {
			watchdog = new ExecuteWatchdog(printJobTimeout);
			executor.setWatchdog(watchdog);
		}

		if (printInBackground) {
			System.out.println("[print] Executing non-blocking print job ... ");
			resultHandler = new PrintResultHandler(watchdog);
			executor.execute(commandLine, resultHandler);
		} else {
			System.out.println("[print] Executing blocking print job ... ");
			exitValue = executor.execute(commandLine);
			resultHandler = new PrintResultHandler(exitValue);
		}

		return resultHandler;
	}

	private class PrintResultHandler extends DefaultExecuteResultHandler {
		private ExecuteWatchdog watchdog;

		public PrintResultHandler(ExecuteWatchdog watchdog) {
			this.watchdog = watchdog;
		}

		public PrintResultHandler(int exitValue) {
			onProcessComplete(exitValue);
		}

		public void onProcessComplete(int exitValue) {
			super.onProcessComplete(exitValue);
			System.out.println("[resultHandler] The document was successfully printed ...");
		}

		public void onProcessFailed(ExecuteException e) {
			super.onProcessFailed(e);
			if (watchdog != null && watchdog.killedProcess()) {
				System.err.println("[resultHandler] The print process timed out");
			} else {
				System.err.println("[resultHandler] The print process failed to do : " + e.getMessage());
			}
		}
	}
}
