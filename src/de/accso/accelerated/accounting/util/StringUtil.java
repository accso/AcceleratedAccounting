package de.accso.accelerated.accounting.util;

public class StringUtil {

	public static boolean isNullOrEmpty(String string){
		if(string == null){
			return true;
		}
		return string.length() == 0;
	}
}
