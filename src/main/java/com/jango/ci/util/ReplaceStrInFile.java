package com.jango.ci.util;

import hudson.model.BuildListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.jango.ci.exception.StringNotFoundException;

/**
 * 
 * @author Jango Chu
 * 
 */
public class ReplaceStrInFile {
	private static BufferedReader bufread;
	private static int aIndex;

    /**
     *
     * @param listener
     * @param filePath
     * @param srcString
     * @param dstString
     * @return
     */
	public boolean replaceInFile(BuildListener listener, String filePath,
			String srcString, String dstString) {
		File filename = new File(filePath);
		String bString = null;
		try {
			bString = readFile(filename);
		} catch (FileNotFoundException e) {
			listener.getLogger().println(e);
			return false;
		} catch (IOException e) {
			listener.getLogger().println("[ERROR]Fail to read the file:"+filePath);
			listener.getLogger().println(e);
			return false;
		}
		String newString = null;
		try {
			newString = replace(srcString, dstString, bString);
		} catch (StringNotFoundException e) {
			listener.getLogger().println("[ERROR]String not found");
			listener.getLogger().println(e);
			return false;
		}
		if (newString != null) {
			try {
				writeFile(filename, newString);
			} catch (FileNotFoundException e) {
				listener.getLogger().println(e);
				return false;
			} catch (IOException e) {
				listener.getLogger().println("[ERROR]Fail to write the file:"+filePath);
				listener.getLogger().println(e);
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readFile(File filename) throws FileNotFoundException,
			IOException {
		StringBuffer readStrBuffer = new StringBuffer();
		String read;
		InputStreamReader fileread = new InputStreamReader(new FileInputStream(
				filename), "UTF-8");
		bufread = new BufferedReader(fileread);
		while ((read = bufread.readLine()) != null) {
			readStrBuffer.append(read);
			readStrBuffer.append("\r\n");
		}
		fileread.close();
		bufread.close();
		return readStrBuffer.toString();
	}

	/**
	 * 
	 * @param filename
	 * @param fileContent
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void writeFile(File filename, String fileContent)
			throws IOException, FileNotFoundException {
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(
				filename), "UTF-8");
		BufferedWriter bufferedWriter = null;
		bufferedWriter = new BufferedWriter(write);
		bufferedWriter.write(fileContent);
		bufferedWriter.close();
		write.close();
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param source
	 * @return
	 * @throws StringNotFoundException
	 */
	public static String replace(String from, String to, String source)
			throws StringNotFoundException {
		if (source == null || from == null || to == null) {
			return null;
		}
		StringBuffer bf = new StringBuffer("");
		String bakString = source;
		aIndex = -1;
		while ((aIndex = source.indexOf(from)) != -1) {
			bf.append(source.substring(0, aIndex) + to);
			source = source.substring(aIndex + from.length());
			aIndex = source.indexOf(from);
		}
		bf.append(source);
		String aString = bf.toString();
		if (aString.equals(bakString)) {
			throw new StringNotFoundException();
		}
		return aString;
	}
}
