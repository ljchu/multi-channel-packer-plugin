package com.jango.ci.util;

import java.util.Map;
/**
 * Resolve the environment of jenkins.
 * @author Jango Chu
 * 2014-3-11
 */
public class EnvResolver {

	private static String left = "${";
	private static String right = "}";

	public static String getLeft() {
		return left;
	}

	public static void setLeft(String left) {
		EnvResolver.left = left;
	}

	public static String getRight() {
		return right;
	}

	public static void setRight(String right) {
		EnvResolver.right = right;
	}
	/**
	 * 
	 * @param map
	 * @param inputString
	 * @return The String after replace the value whit the environment.
	 */
	public static String changeStringWithEnv(Map<?, ?> map, String inputString) {
		StringBuffer aBuffer = new StringBuffer(inputString);
		int aIndex = -1;
		int bIndex = -1;
		StringBuffer bf = null;
		while ((aIndex = aBuffer.indexOf(left)) != -1
				&& ((bIndex = aBuffer.indexOf(right)) != -1)) {
			boolean a = map.containsKey(aBuffer.substring(aIndex + 2, bIndex));
			if (!a) {
				break;
			} else {
				bf = aBuffer
						.replace(aIndex, bIndex + 1, (String) map.get(aBuffer
								.substring(aIndex + 2, bIndex)));
				aBuffer = bf;
				aIndex = aBuffer.indexOf(left);
				bIndex = aBuffer.indexOf(right);
			}
		}

		if (bf != null) {
			return bf.toString();
		} else {
			return inputString;
		}
	}
}
