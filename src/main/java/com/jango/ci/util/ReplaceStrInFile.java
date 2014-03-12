package com.jango.ci.util;

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
	 * @param filePath
	 * @param srcString
	 * @param dstString
	 * @return
	 * @throws StringNotFoundException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void replaceInFile(String filePath, String srcString,
			String dstString) throws StringNotFoundException,
			FileNotFoundException, IOException {
		File filename = new File(filePath);
		String bString = null;
		bString = readFile(filename);
		String newString = replace(srcString, dstString, bString);
		writeFile(filename, newString);
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
		if (source == null || from == null || to == null)
			return null;
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
