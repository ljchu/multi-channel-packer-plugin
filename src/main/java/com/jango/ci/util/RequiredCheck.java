package com.jango.ci.util;

import java.io.File;
import java.io.FileNotFoundException;
import com.jango.ci.exception.StringIsNullException;

public class RequiredCheck {
	public static void checkStringIsNull(String aString) throws StringIsNullException {
		if (aString==null) {
			throw new StringIsNullException(aString);
		}
	}
	
	public static void checkFileIsExist(String filePath) throws FileNotFoundException {
		if (!new File(filePath).exists()) {
			throw new FileNotFoundException();
		}
	}
}
