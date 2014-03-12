package com.jango.ci.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
									"[ERROR]拷贝文件" + srcPath + "到文件夹"
											+ targetPath + "失败.");
							e.printStackTrace();
							return false;
						}
						listener.getLogger().println(
								"[INFO]拷贝文件" + srcPath + "到文件夹" + targetPath);
						return true;
					} else {
						try {
							copyFileToFileForChannel(srcFile, targetFile);
						} catch (Exception e) {
							listener.getLogger().println(
									"[ERROR]覆盖文件" + srcPath + "到文件夹"
											+ targetPath + "失败.");
							e.printStackTrace();
							return false;
						}
						listener.getLogger().println(
								"[INFO]覆盖文件" + srcPath + "到文件" + targetPath);
						return true;
					}
				} else {
					File newFile = smartCreatDir(srcPath, targetPath);
					try {
						copyFileToFileForChannel(srcFile, newFile);
					} catch (Exception e) {
						listener.getLogger().println(
								"[ERROR]拷贝文件" + srcPath + "到" + targetPath
										+ "失败.");
						e.printStackTrace();
						return false;
					}
					listener.getLogger().println(
							"[INFO]拷贝文件" + srcPath + "到" + targetPath);
					return true;
				}
			} else if (targetFile.isFile()) {
				listener.getLogger().println(
						"[ERROR]无法将文件夹" + srcPath + "拷贝到已存在的文件" + targetPath);
				return false;
			} else {
				try {
					copyDirectiory(srcPath, targetPath);
				} catch (Exception e) {
					listener.getLogger().println(
							"[ERROR]拷贝文件夹" + srcPath + "到文件夹" + targetPath
									+ "失败.");
					e.printStackTrace();
					return false;
				}
				listener.getLogger().println(
						"[INFO]拷贝文件夹" + srcPath + "到文件夹" + targetPath);
				return true;
			}
		} else {
			listener.getLogger().println("[ERROR]源文件不存在." + srcPath);
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
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFileToFileForChannel(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}

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
			mkDir(file);
			File newFile = new File(aString);
			return newFile;
		}

	}

	public static void mkDir(File file) {
		if (file.getParentFile().exists()) {
			file.mkdir();
		} else {
			mkDir(file.getParentFile());
			file.mkdir();
		}
	}
}
