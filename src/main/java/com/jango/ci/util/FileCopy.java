package com.jango.ci.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

import hudson.model.BuildListener;

public class FileCopy {
	/**
	 * 
	 * @param listener
	 * @param srcPath
	 * @param targetPath
	 * @return
	 */
	public boolean copyFile(BuildListener listener, String srcPath,
			String targetPath) {
		File srcFile = new File(srcPath);
		File targetFile = new File(targetPath);
		if (srcFile.exists()) {
			if (srcFile.isFile()) {
				if (targetFile.exists()) {
					if (targetFile.isDirectory()) {
						String srcFileName = srcFile.getName();
						File newTargetFile = new File(targetPath + "\\"
								+ srcFileName);
						try {
							copyFileToFileForChannel(srcFile, newTargetFile);
						} catch (Exception e) {
							listener.getLogger().println(
									"[ERROR]Fail to copy" + srcPath
											+ " to folder:" + targetPath);
							e.printStackTrace();
							return false;
						}
						listener.getLogger().println(
								"[INFO]Copy the file:" + srcPath + " to:"
										+ targetPath);
						return true;
					} else {
						try {
							copyFileToFileForChannel(srcFile, targetFile);
						} catch (Exception e) {
							listener.getLogger().println(
									"[ERROR] Fail to cover the file:"
											+ targetPath + " with:" + srcPath);
							e.printStackTrace();
							return false;
						}
						listener.getLogger().println(
								"[INFO]cover the file:" + targetPath + " with:"
										+ srcPath);
						return true;
					}
				} else {
					File newFile = smartCreatDir(srcPath, targetPath);
					try {
						copyFileToFileForChannel(srcFile, newFile);
					} catch (Exception e) {
						listener.getLogger().println(
								"[ERROR]Fail to copy the file:" + srcPath + " to:" + targetPath);
						e.printStackTrace();
						return false;
					}
					listener.getLogger().println(
							"[INFO]Copy the file:" + srcPath + " to:" + targetPath);
					return true;
				}
			} else if (targetFile.isFile()) {
				listener.getLogger().println(
						"[ERROR]Can not copy a folder :" + srcPath + " to a file:" + targetPath);
				return false;
			} else {
				try {
					copyDirectiory(srcPath, targetPath);
				} catch (Exception e) {
					listener.getLogger().println(
							"[ERROR]Fail to copy the folder:" + srcPath + " to the folder:" + targetPath);
					e.printStackTrace();
					return false;
				}
				listener.getLogger().println(
						"[INFO]Copy the folder:" + srcPath + " to the folder:" + targetPath);
				return true;
			}
		} else {
			listener.getLogger().println("[ERROR]Path not exist:" + srcPath);
			return false;
		}
	}

	/**
	 * 
	 * @param srcFile
	 * @param targetFile
	 * @return
	 * @throws Exception
	 */
	public static long copyFileToFileForChannel(File srcFile, File targetFile)
			throws Exception {
		long time = new Date().getTime();
		int length = 2097152;
		@SuppressWarnings("resource")
		FileInputStream in = new FileInputStream(srcFile);
		@SuppressWarnings("resource")
		FileOutputStream out = new FileOutputStream(targetFile);
		FileChannel inC = in.getChannel();
		FileChannel outC = out.getChannel();
		ByteBuffer b = null;
		while (true) {
			if (inC.position() == inC.size()) {
				inC.close();
				outC.close();
				return new Date().getTime() - time;
			}
			if ((inC.size() - inC.position()) < length) {
				length = (int) (inC.size() - inC.position());
			} else
				length = 2097152;
			b = ByteBuffer.allocateDirect(length);
			inC.read(b);
			b.flip();
			outC.write(b);
			outC.force(false);
		}
	}

	/**
	 * 
	 * @param sourceDir
	 * @param targetDir
	 * @throws Exception
	 */
	public static void copyDirectiory(String sourceDir, String targetDir)
			throws Exception {
		(new File(targetDir)).mkdirs();
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				File sourceFile = file[i];
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFileToFileForChannel(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				String dir1 = sourceDir + "/" + file[i].getName();
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}
	/**
	 * 
	 * @param srcPath
	 * @param aString
	 * @return
	 */
	public static File smartCreatDir(String srcPath, String aString) {
		int len = aString.length();
		if (aString.substring(len - 1, len).equals("/")
				|| aString.substring(len - 1, len).equals("\\")) {
			File srcFile = new File(srcPath);
			File file = new File(aString.substring(0, len - 1));
			mkDir(file);
			File newFile = new File(aString + srcFile.getName());
			return newFile;

		} else {
			char[] array = aString.toCharArray();
			Integer aInteger = 0;
			for (int i = array.length - 1; i > -1; i--) {
				if (array[i] == '/' || array[i] == '\\') {
					aInteger = i;
					break;
				}
			}
			File file = new File(aString.substring(0, aInteger));
			File newFile = new File(aString);
			if (aInteger <= 2) {
				return newFile;
			}
			mkDir(file);
			return newFile;
		}
	}
	/**
	 * 
	 * @param file
	 */
	public static void mkDir(File file) {
		if (file.getParentFile().exists()) {
			file.mkdir();
		} else {
			mkDir(file.getParentFile());
			file.mkdir();
		}
	}
}
