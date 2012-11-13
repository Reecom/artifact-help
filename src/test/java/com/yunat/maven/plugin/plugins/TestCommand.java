package com.yunat.maven.plugin.plugins;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class TestCommand {
	public static void main(String[] args) throws IOException,
			InterruptedException {
		String cmdarray = "cmd /c c:\\Users\\john\\base.bat";
		Process process = Runtime.getRuntime().exec(cmdarray);
		InputStreamReader ir = new InputStreamReader(process.getInputStream());
		LineNumberReader input = new LineNumberReader(ir);
		String line;
		while ((line = input.readLine()) != null)
			System.out.println(line);
		input.close();
		ir.close();
		process.waitFor();
		System.out.println("1----------------");
		System.out.println("2----------------");
	}
}
