/**
 * 
 */
package com.simpragma.magicchef.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Swagata
 * 
 */
public class RecipeUtil {
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public static String getAppId(){
		return "YOUR_APP_ID";
	}
	
	public static String getConsumerKey(){
		return "YOUR_CONSUMER_KEY";
	}
	
	public static String getSecretKey(){
		return "YOUR_SECRET_KEY";
	}
}
