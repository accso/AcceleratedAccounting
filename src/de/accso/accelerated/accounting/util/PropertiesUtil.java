package de.accso.accelerated.accounting.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;

public class PropertiesUtil {
	
	private static Properties properties;
	private static boolean dropTablesOnDatabaseUpgradeDefault = false;
	private static boolean isDeveloper = false;
	
	private static void loadProperties(Context context) throws IOException{
		AssetManager assetManager =  context.getResources().getAssets();
		InputStream inputStream = assetManager.open("AcceleratedAccounting.properties");
	    properties = new Properties();
	    properties.load(inputStream);
	}
	
	private static boolean getBoolean(Context context, String key, boolean defaultValue){
		if(properties == null){
			try {
				loadProperties(context);
			} catch (IOException e) {
				e.printStackTrace();
				return defaultValue;
			}
			if(properties == null){
				return defaultValue;
			}
		}
		if(!properties.containsKey(key)){
			return defaultValue;
		}
		return Boolean.parseBoolean(properties.getProperty(key));
	}
	
	public static boolean dropTablesOnDatabaseUpgrade(Context context) {
		return getBoolean(context, "DROP_TABLES_ON_DATABASE_UPGRADE", dropTablesOnDatabaseUpgradeDefault);		
	}
	
	public static boolean isDeveloper(Context context) {
		return getBoolean(context, "IS_DEVELOPER", isDeveloper);		
	}
	
	
}
