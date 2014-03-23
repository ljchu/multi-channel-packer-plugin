package com.jango.ci.util;

import java.util.HashMap;
import java.util.Iterator;
/**
 * 
 * @author Jango Chu
 *
 */
public class CollectionUtil {
    /**
     *
     * @param map
     * @param aInteger
     * @return
     */
	public static String getKeyOfMapByValue(HashMap<String, Integer> map, Integer aInteger) {
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String keyString = it.next();
			if (map.get(keyString).equals(aInteger))
				return keyString;
		}
		return null;
	}
}
