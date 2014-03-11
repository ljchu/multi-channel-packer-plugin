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
	public static boolean writeFile(File filename, String fileContent)
			throws IOException, FileNotFoundException {
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(
				filename), "UTF-8");
		BufferedWriter bufferedWriter = null;
		bufferedWriter = new BufferedWriter(write);
		bufferedWriter.write(fileContent);
		bufferedWriter.close();
		write.close();
		return true;
	}
	/**
	 * 
	 * @param filePath
	 * @param srcString
	 * @param dstString
	 * @return
	 */
	public boolean replaceInFile(String filePath, String srcString,
			String dstString) {
		File filename = new File(filePath);
		String bString = null;
		try {
			bString = readFile(filename);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}

		String newString = replace(srcString, dstString, bString);
		boolean writeResult = false;
		try {
			writeResult = writeFile(filename, newString);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (writeResult) {
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param from
	 * @param to
	 * @param source
	 * @return
	 */
	public static String replace(String from, String to, String source) {
		if (source == null || from == null || to == null)
			return null;
		StringBuffer bf = new StringBuffer("");
		aIndex = -1;
		while ((aIndex = source.indexOf(from)) != -1) {
			bf.append(source.substring(0, aIndex) + to);
			source = source.substring(aIndex + from.length());
			aIndex = source.indexOf(from);
		}
		bf.append(source);
		return bf.toString();
	}
}
